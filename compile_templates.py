import os
import re

template_dir = r"c:\Users\Hp\Downloads\Project Code Capstone 2025\frontend-service\src\main\resources\templates"

# Read headers
with open(os.path.join(template_dir, "commonheader.html"), "r", encoding="utf-8") as f:
    common_header = f.read()
    
with open(os.path.join(template_dir, "userheader.html"), "r", encoding="utf-8") as f:
    user_header = f.read()

def inject_content(header_html, content_html):
    # Find {% block content %} ... {% endblock %} in header_html and replace with content_html
    return re.sub(r'\{%\s*block\s+content\s*%\}\s*\{%\s*endblock\s*%\}', content_html, header_html)

for filename in os.listdir(template_dir):
    if filename.endswith(".html") and filename not in ["commonheader.html", "userheader.html", "detector.html"]:
        filepath = os.path.join(template_dir, filename)
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
            
        merge_needed = False
        target_header = None
        
        # Check if extends commonheader or userheader
        if "extends 'commonheader.html'" in content:
            merge_needed = True
            target_header = common_header
        elif "extends 'userheader.html'" in content:
            merge_needed = True
            target_header = user_header
            
        if merge_needed:
            # Extract content between {% block content %} and {% endblock %}
            match = re.search(r'\{%\s*block\s+content\s*%\}(.*?)\{%\s*endblock\s*%\}', content, re.DOTALL)
            if match:
                inner_content = match.group(1)
                new_content = inject_content(target_header, inner_content)
                
                # Write back
                with open(filepath, "w", encoding="utf-8") as f:
                    f.write(new_content)
                print(f"Processed {filename}")
            else:
                print(f"Failed to find block content match in {filename}")

# Also replace any stray Jinja variable syntax that Spring Boot won't like, e.g. {{ msg }} -> <span th:text="${msg}"></span>
# Actually we can just do a simple replacement for {{ msg }} with Thymeleaf inline or th:text later if needed.
# But looking at FrontendController, I added msg to the Model, so we DO need to handle `{{ msg }}` or `{{msg}}`.
for filename in os.listdir(template_dir):
    if filename.endswith(".html"):
        filepath = os.path.join(template_dir, filename)
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
            
        new_content = re.sub(r'\{\{\s*msg\s*\}\}', r'<span th:text="${msg}"></span>', content)
        new_content = re.sub(r'\{\{\s*data\[\'([^\']+)\'\]\s*\}\}', r'<span th:text="${data[\'\1\']}"></span>', new_content)

        if str(content) != str(new_content):
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(new_content)
            print(f"Replaced Thymeleaf variables in {filename}")
