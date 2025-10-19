# Python Virtual Environment Setup

Quick guide to Python virtual environments.

## Using venv (Built-in)

```bash
# Create virtual environment
python -m venv venv

# Or for Python 3 specifically
python3 -m venv venv

# Activate (Linux/Mac)
source venv/bin/activate

# Activate (Windows)
venv\Scripts\activate

# Deactivate
deactivate
```

## Using virtualenv

```bash
# Install virtualenv
pip install virtualenv

# Create virtual environment
virtualenv venv

# With specific Python version
virtualenv -p python3.11 venv

# Activate (Linux/Mac)
source venv/bin/activate

# Activate (Windows)
venv\Scripts\activate
```

## Using Poetry

```bash
# Install Poetry
curl -sSL https://install.python-poetry.org | python3 -

# Create new project
poetry new myproject
cd myproject

# Or initialize in existing directory
poetry init

# Add dependency
poetry add requests

# Add dev dependency
poetry add --group dev pytest

# Install dependencies
poetry install

# Run script in environment
poetry run python script.py

# Activate shell
poetry shell
```

## Using Conda

```bash
# Create environment
conda create -n myenv python=3.11

# Activate environment
conda activate myenv

# Deactivate
conda deactivate

# List environments
conda env list

# Remove environment
conda env remove -n myenv

# Export environment
conda env export > environment.yml

# Create from file
conda env create -f environment.yml
```

## Project Setup with venv

```bash
# Complete setup
mkdir myproject
cd myproject

# Create venv
python -m venv venv
source venv/bin/activate  # or venv\Scripts\activate on Windows

# Upgrade pip
pip install --upgrade pip

# Install dependencies
pip install -r requirements.txt

# Or install common packages
pip install requests pandas numpy flask
```

## .gitignore for Python

```gitignore
# Virtual environment
venv/
env/
ENV/
.venv/

# Python cache
__pycache__/
*.py[cod]
*$py.class
*.so

# Distribution
dist/
build/
*.egg-info/

# Testing
.pytest_cache/
.coverage
htmlcov/

# IDE
.vscode/
.idea/
*.swp
*.swo

# Environment
.env
.env.local
```

## Quick Start Script

```bash
#!/bin/bash
# setup.sh

echo "Setting up Python project..."

# Create virtual environment
python3 -m venv venv

# Activate
source venv/bin/activate

# Upgrade pip
pip install --upgrade pip

# Install dependencies
if [ -f requirements.txt ]; then
    pip install -r requirements.txt
fi

echo "Setup complete! Run 'source venv/bin/activate' to activate."
```

```bash
chmod +x setup.sh
./setup.sh
```

## requirements.txt from Environment

```bash
# Freeze current packages
pip freeze > requirements.txt

# Install from file
pip install -r requirements.txt

# Upgrade all packages
pip list --outdated
pip install -U package-name
```
