from flask import Flask, request, jsonify
import os
from groq import Groq
from pdfminer.high_level import extract_text
from deep_translator import GoogleTranslator
import tempfile

app = Flask(__name__)

os.environ['GROQ_API_KEY'] = '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'
client = Groq(api_key=os.environ.get("GROQ_API_KEY"))

def translate(text):
    """Translate text from French to English"""
    translated = GoogleTranslator(source='fr', target='en').translate(text)
    return translated

def parsingExperienceType(resume_text, ExperienceType):
    """Extract specific experience type from resume with improved prompting"""
    prompt = f"""You are an expert resume parser. Your task is to extract all {ExperienceType} information from the following resume text.
    Focus only on extracting relevant {ExperienceType} details, excluding dates and locations.
    Format your response as clean, concise bullet points without any additional commentary.

    Resume text:
    {resume_text}
    """
    
    response = client.chat.completions.create(
        messages=[{
            "role": "user",
            "content": prompt,
        }],
        model="llama-3.3-70b-specdec",
        temperature=0.1,  # Lower temperature for more focused extraction
        max_tokens=1000
    )
    return response.choices[0].message.content

def parsingOutputFormatting(resume_text, ExperienceType):
    """Format the extracted experience data with improved prompting"""
    prompt = f"""Format the following {ExperienceType} information into a standardized list.
    Rules:
    1. Separate each item with ' ~ '
    2. Remove any bullet points, numbers, or special characters
    3. Keep only the essential information
    4. If no relevant information is found, return an empty string
    5. Do not include any explanatory text or comments

    Input text:
    {resume_text}
    """
    
    response = client.chat.completions.create(
        messages=[{
            "role": "user",
            "content": prompt,
        }],
        model="llama-3.3-70b-specdec",
        temperature=0.1
    )
    return response.choices[0].message.content.strip()

def parsed(text, ExperienceType):
    """Complete parsing pipeline for a single experience type"""
    translated_text = translate(text)
    extracted_info = parsingExperienceType(translated_text, ExperienceType)
    return parsingOutputFormatting(extracted_info, ExperienceType)

def combine(text):
    """Combine all experience types into a single structured output"""
    education = parsed(text, 'education')
    work = parsed(text, 'professional work experience')
    projects = parsed(text, 'project')
    extra = parsed(text, 'extracurricular activities')
    
    # Clean and filter empty entries
    def clean_split(text):
        return [{'description': desc.strip()} for desc in text.split(' ~ ') if desc.strip()]
    
    combined_data = {
        'education': clean_split(education),
        'work': clean_split(work),
        'project': clean_split(projects),
        'extra': clean_split(extra)
    }
    return combined_data

@app.route('/parse-resume', methods=['POST'])
def parse_resume():
    """Endpoint to handle resume parsing requests"""
    if 'file' not in request.files:
        return jsonify({'error': 'No file provided'}), 400
    
    file = request.files['file']
    if not file.filename.endswith('.pdf'):
        return jsonify({'error': 'Only PDF files are supported'}), 400
    
    try:
        with tempfile.NamedTemporaryFile(suffix='.pdf', delete=False) as temp_file:
            file.save(temp_file.name)
            text = extract_text(temp_file.name)
        
        parsed_data = combine(text)
        return jsonify(parsed_data)
            
    except Exception as e:
        print(f"Error in parsing endpoint: {e}")
        return jsonify({'error': str(e)}), 500
    
if __name__ == '__main__':
    app.run(port=5000)