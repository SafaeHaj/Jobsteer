from flask import Flask, request, jsonify
import os
from groq import Groq
from pdfminer.high_level import extract_text
from deep_translator import GoogleTranslator
import tempfile
import json

app = Flask(__name__)

os.environ['GROQ_API_KEY'] = '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'
client = Groq(
    api_key=os.environ.get("GROQ_API_KEY"),
)

def translate(text):
    """Translate text from French to English"""
    translated = GoogleTranslator(source='fr', target='en').translate(text)
    return translated

def parsingExperienceType(resume_text, ExperienceType):
    """Extract specific experience type from resume"""
    response = client.chat.completions.create(
        messages=[{
            "role": "user",
            f"content": f"Extract the {ExperienceType} section from the following resume, don't include the dates and locations.:\n\n{resume_text}",
        }],
        model="llama3-groq-70b-8192-tool-use-preview",
    )
    return response.choices[0].message.content

def parsingOutputFormatting(resume_text, ExperienceType):
    """Format the extracted experience data"""
    response = client.chat.completions.create(
        messages=[{
            "role": "user",
            "content": (
                "If the input text is None, return None. Otherwise, format follows "
                f"Each {ExperienceType} description should be added to the list of the output, "
                f"If the {ExperienceType} section is missing or contains no relevant data, "
                "set the description to an empty string or None if not applicable."
                "\nThe output format should look like this:\n"
                f"{ExperienceType} description ~ {ExperienceType} description ~ ..."
                "Please do not include any comments or extra information; just return the clean output list. "
                f"\n\nResume Text:\n{resume_text}"
            ),
        }],
        model="llama3-groq-70b-8192-tool-use-preview",
    )
    return response.choices[0].message.content

def parsed(text, ExperienceType):
    """Complete parsing pipeline for a single experience type"""
    return parsingOutputFormatting(parsingExperienceType(translate(text), ExperienceType), ExperienceType)

def combine(text):
    """Combine all experience types into a single structured output"""
    ed = parsed(text, 'education')
    work = parsed(text, 'professional work experience')
    projects = parsed(text, 'project')
    extra = parsed(text, 'extracurricular')
    
    combined_data = {
        'education': [{'description': desc} for desc in ed.split(' ~ ') if desc],
        'work': [{'description': desc} for desc in work.split(' ~ ') if desc],
        'project': [{'description': desc} for desc in projects.split(' ~ ') if desc],
        'extra': [{'description': desc} for desc in extra.split(' ~ ') if desc]
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
    
    temp_dir = None
    try:
        temp_dir = tempfile.mkdtemp()
        temp_file_path = os.path.join(temp_dir, 'temp_resume.pdf')
        
        file.save(temp_file_path)
        
        text = extract_text(temp_file_path)
        
        parsed_data = combine(text)
        
        return jsonify(parsed_data)
            
    except Exception as e:
        return jsonify({'error': str(e)}), 500
   
if __name__ == '__main__':
    app.run(port=5000)