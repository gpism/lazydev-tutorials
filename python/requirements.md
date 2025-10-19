# Requirements.txt Best Practices

How to manage Python dependencies properly.

## Basic requirements.txt

```txt
# requirements.txt
requests==2.31.0
flask==3.0.0
pytest==7.4.2
python-dotenv==1.0.0
```

## With Version Ranges

```txt
# Allow patch updates
requests>=2.31.0,<2.32.0

# Allow minor updates
flask>=3.0.0,<4.0.0

# Exact version
pytest==7.4.2

# Minimum version
numpy>=1.24.0

# Compatible release
pandas~=2.0.0  # Equivalent to >=2.0.0,<2.1.0
```

## Organized by Category

```txt
# requirements.txt

# Web Framework
flask==3.0.0
gunicorn==21.2.0

# Database
sqlalchemy==2.0.21
psycopg2-binary==2.9.7

# API & HTTP
requests==2.31.0
httpx==0.24.1

# Data Processing
pandas==2.1.1
numpy==1.25.2

# Environment
python-dotenv==1.0.0

# Testing (dev)
pytest==7.4.2
pytest-cov==4.1.0
```

## Multiple Requirements Files

### requirements/base.txt
```txt
# Core dependencies
flask==3.0.0
sqlalchemy==2.0.21
python-dotenv==1.0.0
```

### requirements/dev.txt
```txt
# Development dependencies
-r base.txt

pytest==7.4.2
pytest-cov==4.1.0
black==23.9.1
flake8==6.1.0
mypy==1.5.1
```

### requirements/prod.txt
```txt
# Production dependencies
-r base.txt

gunicorn==21.2.0
psycopg2-binary==2.9.7
```

```bash
# Install for development
pip install -r requirements/dev.txt

# Install for production
pip install -r requirements/prod.txt
```

## Flask API Project

```txt
# requirements.txt

# Web Framework
flask==3.0.0
flask-cors==4.0.0
flask-sqlalchemy==3.1.1

# Database
psycopg2-binary==2.9.7
alembic==1.12.0

# Authentication
flask-jwt-extended==4.5.2
bcrypt==4.0.1

# Validation
marshmallow==3.20.1

# Environment
python-dotenv==1.0.0

# Production Server
gunicorn==21.2.0

# Testing
pytest==7.4.2
pytest-flask==1.2.0
```

## Django Project

```txt
# requirements.txt

# Framework
django==4.2.5
djangorestframework==3.14.0

# Database
psycopg2-binary==2.9.7

# Authentication
djangorestframework-simplejwt==5.3.0

# CORS
django-cors-headers==4.2.0

# Environment
python-decouple==3.8

# Production
gunicorn==21.2.0
whitenoise==6.5.0

# Testing
pytest-django==4.5.2
factory-boy==3.3.0
```

## Data Science Project

```txt
# requirements.txt

# Core
numpy==1.25.2
pandas==2.1.1
scipy==1.11.3

# Visualization
matplotlib==3.8.0
seaborn==0.13.0
plotly==5.17.0

# Machine Learning
scikit-learn==1.3.1
tensorflow==2.14.0
torch==2.1.0

# Jupyter
jupyter==1.0.0
ipython==8.16.1
jupyterlab==4.0.6

# Utilities
python-dotenv==1.0.0
tqdm==4.66.1
```

## FastAPI Project

```txt
# requirements.txt

# Framework
fastapi==0.103.2
uvicorn[standard]==0.23.2

# Database
sqlalchemy==2.0.21
alembic==1.12.0
asyncpg==0.28.0

# Authentication
python-jose[cryptography]==3.3.0
passlib[bcrypt]==1.7.4
python-multipart==0.0.6

# Validation
pydantic==2.4.2
pydantic-settings==2.0.3

# HTTP
httpx==0.24.1

# Testing
pytest==7.4.2
pytest-asyncio==0.21.1
```

## Generating requirements.txt

```bash
# Basic freeze
pip freeze > requirements.txt

# Using pipreqs (only imports)
pip install pipreqs
pipreqs . --force

# Using pip-tools
pip install pip-tools
pip-compile requirements.in

# Poetry to requirements.txt
poetry export -f requirements.txt --output requirements.txt
```

## Updating Dependencies

```bash
# Check outdated packages
pip list --outdated

# Update specific package
pip install --upgrade package-name

# Update all (be careful!)
pip list --outdated --format=freeze | cut -d = -f 1 | xargs -n1 pip install -U

# Using pip-review
pip install pip-review
pip-review --local --interactive
```

## Lock File Pattern

```txt
# requirements.in (loose constraints)
flask>=3.0
sqlalchemy>=2.0
requests>=2.30
```

```bash
# Generate locked requirements.txt
pip-compile requirements.in

# This creates requirements.txt with exact versions
# Including all sub-dependencies
```
