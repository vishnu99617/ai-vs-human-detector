import os
import re

template_dir = r"c:\Users\Hp\Downloads\Project Code Capstone 2025\frontend-service\src\main\resources\templates"

for filename in os.listdir(template_dir):
    if filename.endswith(".html"):
        filepath = os.path.join(template_dir, filename)
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
            
        # Replace href="/static/..." with href="/..."
        new_content = content.replace("href=\"/static/", "href=\"/")
        new_content = new_content.replace("src=\"/static/", "src=\"/")
        
        if content != new_content:
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(new_content)
            print(f"Fixed static paths in {filename}")
