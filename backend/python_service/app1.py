from flask import Flask, jsonify
import os
from groq import Groq
from deep_translator import GoogleTranslator
import requests
from typing import List, Dict, Any

app = Flask(__name__)

class JobScraper:
    def __init__(self):
        self.groq_api_key = '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'
        self.client = Groq(api_key=self.groq_api_key)
        
        self.translator = GoogleTranslator(source='fr', target='en')
        
        self.api_url = "https://api.theirstack.com/v1/jobs/search"
        self.api_headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzYWZhZS5oYWpAZ21haWwuY29tIiwicGVybWlzc2lvbnMiOiJ1c2VyIn0.l-rDEoIdoSCM30W_bS3EREJ4_OzImWW-VH7l1yRK4MM"
        }

    def fetch_jobs(self) -> List[Dict]:
        """Fetch jobs from the API and translate them"""
        payload = {
            "page": 0,
            "limit": 10,
            "posted_at_max_age_days": 15,
            "order_by": [{"desc": True, "field": "date_posted"}],
            "job_country_code_or": ["MA"],
            "include_total_results": False,
            "blur_company_data": False
        }

        response = requests.post(self.api_url, headers=self.api_headers, json=payload)
        if response.status_code != 200:
            raise Exception(f"API request failed with status code {response.status_code}")

        jobs_list = []
        data = response.json().get("data", [])

        for job in data:
            translated_job = self._translate_job(job)
            if translated_job:
                jobs_list.append(translated_job)

        return jobs_list

    def _translate_job(self, job: Dict) -> Dict:
        """Translate job fields from French to English"""
        def safe_translate(text: str) -> str:
            if text and isinstance(text, str):
                try:
                    return self.translator.translate(text)
                except Exception as e:
                    print(f"Translation error: {e}")
                    return text
            return text or ""

        return {
            "title": safe_translate(job.get("job_title")),
            "description": safe_translate(job.get("description")),
            "final_url": job.get("final_url"),
            "location": safe_translate(job.get("location")),
            "language": safe_translate(job.get("language"))
        }
    def extract_fields(self, jobs: List[Dict]) -> List[Dict]:
        """Extract structured fields from job descriptions"""
        extracted_data = []

        for job in jobs:
            if not job.get('description'):
                continue

            prompt = f"""
            Extract the following fields concisely from the text below, no comment on anything:

            EXPERIENCE: Relevant job titles or roles
            EDUCATION: Degree type and field of study
            SKILL: Key hard/soft skills, tools, or expertise
            LANGUAGE: Languages mentioned
            DESCRIPTION: Summary or relevant personal/professional details
            LOCATION: Format as "city, country"

            Text:
            {job['description']}
            """

            try:
                response = self.client.chat.completions.create(
                    messages=[{"role": "user", "content": prompt}],
                    model="llama-3.3-70b-versatile",
                )

                parsed_data = {}
                for line in response.choices[0].message.content.split("\n"):
                    key_value = line.split(":", 1)
                    if len(key_value) == 2:
                        key, value = key_value
                        parsed_data[key.strip().upper()] = [v.strip() for v in value.split(',')]

                combined_entry = {**job, **parsed_data}

                combined_entry['EXPERIENCE'] = ', '.join(combined_entry.get('EXPERIENCE', []))
                combined_entry['EDUCATION'] = ', '.join(combined_entry.get('EDUCATION', []))
                combined_entry['SKILL'] = ', '.join(combined_entry.get('SKILL', []))
                combined_entry['LANGUAGE'] = ', '.join(combined_entry.get('LANGUAGE', []))
                combined_entry['DESCRIPTION'] = ', '.join(combined_entry.get('DESCRIPTION', []))

                extracted_data.append(combined_entry)

            except Exception as e:
                print(f"Error processing job: {e}")
                continue

        return extracted_data


scraper = JobScraper()

@app.route('/get-jobs', methods=['GET'])
def get_jobs():
    try:
        print("Fetching and translating jobs...")
        jobs = scraper.fetch_jobs()
        print(f"Retrieved {len(jobs)} jobs")
        
        print("Extracting structured fields...")
        processed_jobs = scraper.extract_fields(jobs)
        print(f"Processed {len(processed_jobs)} jobs")
        
        formatted_jobs = []
        for job in processed_jobs:
            formatted_jobs.append({
                'title': job.get('title'),
                'final_url': job.get('final_url'),
                'location': job.get('location'),
                'language': job.get('language', 'No Language'),
                'EXPERIENCE': job.get('EXPERIENCE', 'Not specified'),
                'EDUCATION': job.get('EDUCATION', 'Not specified'),
                'SKILL': job.get('SKILL', 'Not specified'),
                'LANGUAGE': job.get('LANGUAGE', 'Not specified'),
                'DESCRIPTION': job.get('DESCRIPTION', 'Not specified'),
                'LOCATION': job.get('LOCATION', 'Not mentioned')
            })
        
        return jsonify(formatted_jobs)
        
    except Exception as e:
        print(f"Error processing request: {e}")
        return jsonify({
            "status": "error",
            "message": str(e)
        }), 500

@app.route('/health', methods=['GET'])
def health_check():
    """Simple health check endpoint"""
    return jsonify({"status": "healthy"})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5002)
