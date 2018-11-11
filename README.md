# crypto-trivium

## Generate Standalone Windows App

Steps to generate an standalone Windows app using *pyinstaller*
``` 
pip install pyinstaller 
pyinstaller --onefile app.py
```
After this a app.exe is going to be generated inside the dist folder. Running the app.exe will work even without having python installed. 
