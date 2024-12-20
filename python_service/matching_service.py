from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
from typing import List, Dict, Any

app = Flask(__name__)

model = SentenceTransformer('all-MiniLM-L6-v2')

class MatchingService:
    def __init__(self):
        self.weights = {
            'title': 0.3,
            'experience': 0.25,
            'education': 0.2,
            'language': 0.25
        }

    def calculate_text_similarity(self, text1: str, text2: str) -> float:
        """Calculate semantic similarity between two texts using BERT embeddings"""
        try:
            if not text1 or not text2:
                return 0.0
            embeddings = model.encode([text1, text2])
            similarity = cosine_similarity([embeddings[0]], [embeddings[1]])[0][0]
            return float(similarity)
        except Exception as e:
            print(f"Error calculating text similarity: {e}")
            return 0.0

    def calculate_location_score(self, job_location: str, candidate_location: str) -> float:
        """Simple location matching"""
        if not job_location or not candidate_location:
            return 0.0
        return 1.0 if job_location.lower() == candidate_location.lower() else 0.0

    def match_job_title(self, job_title: str, experiences: List[Dict[str, Any]]) -> float:
        """Match job title with candidate's experiences"""
        max_score = 0.0
        work_experiences = [exp for exp in experiences if exp.get('type') == 'WORK']

        for experience in work_experiences:
            if description := experience.get('description'):
                similarity = self.calculate_text_similarity(job_title, description)
                max_score = max(max_score, similarity)

        return max_score

    def match_requirements(self, requirements: List[Dict[str, Any]], 
                         experiences: List[Dict[str, Any]], 
                         languages: List[str]) -> Dict[str, float]:
        """Match job requirements with candidate's qualifications"""
        scores = {
            'experience': 0.0,
            'education': 0.0,
            'language': 0.0
        }
        
        counts = dict(scores)
        
        for req in requirements:
            req_type = req.get('type')
            req_desc = req.get('description', '').lower()
            
            if req_type == 'EXPERIENCE':  
                work_experiences = [exp.get('description', '') for exp in experiences if exp.get('type') == 'WORK']
                if work_experiences:
                    max_score = max((self.calculate_text_similarity(req_desc, exp) for exp in work_experiences))
                    scores['experience'] += max_score
                    counts['experience'] += 1
                
            elif req_type == 'EDUCATION':
                edu_experiences = [exp.get('description', '') for exp in experiences if exp.get('type') == 'EDUCATION']
                if edu_experiences:
                    max_score = max((self.calculate_text_similarity(req_desc, exp) for exp in edu_experiences))
                    scores['education'] += max_score
                    counts['education'] += 1
                
            elif req_type == 'LANGUAGE':
                if languages:
                    has_language = any(lang.lower() == req_desc.lower() for lang in languages)
                    scores['language'] += float(has_language)
                    counts['language'] += 1
        
        for key in scores:
            if counts[key] > 0:
                scores[key] = scores[key] / counts[key]
                
        return scores

    def calculate_match_score(self, job_post: Dict[str, Any], 
                            resume: Dict[str, Any]) -> Dict[str, Any]:
        """Calculate overall match score between a job post and a resume"""
        try:
            print("Job Post:", job_post)  # Debug print
            print("Resume:", resume)      # Debug print
            
            title = job_post.get('title', '')
            location = job_post.get('location', '')
            requirements = job_post.get('requirements', [])
            experiences = resume.get('experiences', [])
            languages = [lang.get('language', '') for lang in resume.get('languages', [])]
            candidate_location = resume.get('jobSeeker', {}).get('location', '')

            title_score = self.match_job_title(title, experiences)
            requirement_scores = self.match_requirements(requirements, experiences, languages)
            location_score = self.calculate_location_score(location, candidate_location)

            total_score = (
                self.weights['title'] * title_score +
                self.weights['experience'] * requirement_scores['experience'] +
                self.weights['education'] * requirement_scores['education'] +
                self.weights['language'] * requirement_scores['language']
            )

            return {
                'total_score': total_score,
                'component_scores': {
                    'title_match': title_score,
                    'location_match': location_score,
                    **requirement_scores
                },
                'job_post_id': job_post.get('id')
            }
        except Exception as e:
            print(f"Error calculating match score: {e}")
            return {
                'total_score': 0.0,
                'component_scores': {},
                'job_post_id': job_post.get('id')
            }

    def rank_jobs(self, resume: Dict[str, Any], job_posts: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """Rank job posts by compatibility with the given resume"""
        try:
            results = []
            for job in job_posts:
                result = self.calculate_match_score(job, resume)
                results.append(result)
            return sorted(results, key=lambda x: x['total_score'], reverse=True)
        except Exception as e:
            print(f"Error ranking jobs: {e}")
            return []
    
    def rank_resumes(self, job_post: Dict[str, Any], resumes: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """Rank resumes by compatibility with the given job post"""
        try:
            results = []
            for resume in resumes:
                result = self.calculate_match_score(job_post, resume)
                result['resume_id'] = resume.get('id')  # Add resume ID to the result
                results.append(result)
            return sorted(results, key=lambda x: x['total_score'], reverse=True)
        except Exception as e:
            print(f"Error ranking resumes: {e}")
            return []

matching_service = MatchingService()

@app.route('/match-jobs', methods=['POST'])
def match_jobs():
    """Endpoint to match a resume with job posts"""
    try:
        data = request.get_json()
        print("Received data:", data)  # Debug print
        
        resume = data.get('resume')
        job_posts = data.get('job_posts', [])
        
        if not resume:
            raise ValueError("No resume provided")
        
        print(f"Processing {len(job_posts)} job posts")  # Debug print
        ranked_jobs = matching_service.rank_jobs(resume, job_posts)
        print("Ranking complete")  # Debug print
        
        return jsonify({
            'status': 'success',
            'matches': ranked_jobs
        })
        
    except Exception as e:
        print(f"Error in match_jobs endpoint: {e}")
        return jsonify({
            'status': 'error',
            'message': str(e)
        }), 500
        
        
@app.route('/match-candidates', methods=['POST'])
def match_resumes():
    """Endpoint to match resumes to a job post"""
    try:
        data = request.get_json()
        print("Received data:", data)  # Debug print
        
        job_post = data.get('job_post')
        resumes = data.get('resumes', [])
        
        if not job_post:
            raise ValueError("No job post provided")
        
        print(f"Processing {len(resumes)} resumes")  # Debug print
        ranked_resumes = matching_service.rank_resumes(job_post, resumes)
        print("Ranking complete")  # Debug print
        
        return jsonify({
            'status': 'success',
            'matches': ranked_resumes
        })
        
    except Exception as e:
        print(f"Error in match_resumes endpoint: {e}")
        return jsonify({
            'status': 'error',
            'message': str(e)
        }), 500

if __name__ == '__main__':
    app.run(port=5001, debug=True)