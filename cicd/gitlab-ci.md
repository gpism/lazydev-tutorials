# GitLab CI Examples

Ready-to-use GitLab CI/CD configurations.

## Basic Node.js Pipeline

```yaml
# .gitlab-ci.yml
image: node:18

stages:
  - test
  - build
  - deploy

cache:
  paths:
    - node_modules/

before_script:
  - npm ci

test:
  stage: test
  script:
    - npm run lint
    - npm run test
  coverage: '/Coverage: \d+\.\d+/'

build:
  stage: build
  script:
    - npm run build
  artifacts:
    paths:
      - dist/
    expire_in: 1 week

deploy:
  stage: deploy
  script:
    - npm run deploy
  only:
    - main
```

## Python Pipeline

```yaml
# .gitlab-ci.yml
image: python:3.11

stages:
  - test
  - build
  - deploy

cache:
  paths:
    - .cache/pip
    - venv/

before_script:
  - python -m venv venv
  - source venv/bin/activate
  - pip install -r requirements.txt

test:
  stage: test
  script:
    - flake8 .
    - pytest --cov=./ --cov-report=xml
  coverage: '/TOTAL.*\s+(\d+%)$/'

build:
  stage: build
  script:
    - python setup.py build
  artifacts:
    paths:
      - build/

deploy:
  stage: deploy
  script:
    - python setup.py install
  only:
    - main
```

## Docker Build & Push

```yaml
# .gitlab-ci.yml
stages:
  - build
  - deploy

variables:
  DOCKER_DRIVER: overlay2
  IMAGE_TAG: $CI_REGISTRY_IMAGE:$CI_COMMIT_REF_SLUG

build:
  stage: build
  image: docker:latest
  services:
    - docker:dind
  before_script:
    - docker login -u $CI_REGISTRY_USER -p $CI_REGISTRY_PASSWORD $CI_REGISTRY
  script:
    - docker build -t $IMAGE_TAG .
    - docker push $IMAGE_TAG
  only:
    - main
    - tags

deploy:
  stage: deploy
  image: alpine:latest
  before_script:
    - apk add --no-cache openssh-client
    - eval $(ssh-agent -s)
    - echo "$SSH_PRIVATE_KEY" | ssh-add -
    - mkdir -p ~/.ssh
    - ssh-keyscan $DEPLOY_SERVER >> ~/.ssh/known_hosts
  script:
    - ssh $DEPLOY_USER@$DEPLOY_SERVER "docker pull $IMAGE_TAG && docker-compose up -d"
  only:
    - main
```

## Multi-Stage Pipeline

```yaml
# .gitlab-ci.yml
stages:
  - lint
  - test
  - build
  - deploy-staging
  - deploy-production

lint:
  stage: lint
  image: node:18
  script:
    - npm ci
    - npm run lint

unit-test:
  stage: test
  image: node:18
  script:
    - npm ci
    - npm run test:unit
  coverage: '/Lines\s*:\s*(\d+\.\d+)%/'

integration-test:
  stage: test
  image: node:18
  services:
    - postgres:15
  variables:
    POSTGRES_DB: testdb
    POSTGRES_USER: user
    POSTGRES_PASSWORD: password
  script:
    - npm ci
    - npm run test:integration

build:
  stage: build
  image: node:18
  script:
    - npm ci
    - npm run build
  artifacts:
    paths:
      - dist/
    expire_in: 1 week

deploy-staging:
  stage: deploy-staging
  script:
    - echo "Deploying to staging"
    - scp -r dist/* $STAGING_SERVER:/var/www/app/
  environment:
    name: staging
    url: https://staging.example.com
  only:
    - develop

deploy-production:
  stage: deploy-production
  script:
    - echo "Deploying to production"
    - scp -r dist/* $PROD_SERVER:/var/www/app/
  environment:
    name: production
    url: https://example.com
  when: manual
  only:
    - main
```

## Monorepo Pipeline

```yaml
# .gitlab-ci.yml
stages:
  - test
  - build
  - deploy

variables:
  API_DIR: packages/api
  WEB_DIR: packages/web

test-api:
  stage: test
  image: node:18
  script:
    - cd $API_DIR
    - npm ci
    - npm run test
  only:
    changes:
      - packages/api/**/*

test-web:
  stage: test
  image: node:18
  script:
    - cd $WEB_DIR
    - npm ci
    - npm run test
  only:
    changes:
      - packages/web/**/*

build-api:
  stage: build
  image: node:18
  script:
    - cd $API_DIR
    - npm ci
    - npm run build
  artifacts:
    paths:
      - packages/api/dist/
  only:
    changes:
      - packages/api/**/*

build-web:
  stage: build
  image: node:18
  script:
    - cd $WEB_DIR
    - npm ci
    - npm run build
  artifacts:
    paths:
      - packages/web/dist/
  only:
    changes:
      - packages/web/**/*
```

## With Database Service

```yaml
# .gitlab-ci.yml
test:
  stage: test
  image: node:18
  services:
    - postgres:15
    - redis:7
  variables:
    POSTGRES_DB: testdb
    POSTGRES_USER: testuser
    POSTGRES_PASSWORD: testpass
    REDIS_HOST: redis
  script:
    - npm ci
    - npm run test
```

## Scheduled Pipeline

```yaml
# .gitlab-ci.yml
backup:
  stage: backup
  script:
    - ./scripts/backup.sh
  only:
    - schedules
  artifacts:
    paths:
      - backups/
    expire_in: 30 days

cleanup:
  stage: cleanup
  script:
    - ./scripts/cleanup.sh
  only:
    - schedules
  when: always
```

## Using Templates

```yaml
# .gitlab-ci.yml
include:
  - template: Security/SAST.gitlab-ci.yml
  - template: Security/Dependency-Scanning.gitlab-ci.yml
  - template: Security/License-Scanning.gitlab-ci.yml

stages:
  - test
  - build
  - security
  - deploy

test:
  stage: test
  script:
    - npm test
```

## Artifacts & Caching

```yaml
# .gitlab-ci.yml
build:
  stage: build
  script:
    - npm run build
  cache:
    key: ${CI_COMMIT_REF_SLUG}
    paths:
      - node_modules/
  artifacts:
    name: "$CI_JOB_NAME-$CI_COMMIT_REF_NAME"
    paths:
      - dist/
    expire_in: 1 week
    reports:
      coverage_report:
        coverage_format: cobertura
        path: coverage/cobertura-coverage.xml
```

## Manual & Conditional Jobs

```yaml
# .gitlab-ci.yml
deploy-staging:
  stage: deploy
  script:
    - echo "Deploying to staging"
  only:
    - develop
  when: on_success

deploy-production:
  stage: deploy
  script:
    - echo "Deploying to production"
  only:
    - main
  when: manual
  allow_failure: false

rollback:
  stage: deploy
  script:
    - echo "Rolling back"
  when: manual
  only:
    - main
```

## Using Variables

```yaml
# .gitlab-ci.yml
variables:
  GLOBAL_VAR: "global value"

job1:
  variables:
    JOB_VAR: "job value"
  script:
    - echo $GLOBAL_VAR
    - echo $JOB_VAR
    - echo $CI_COMMIT_SHA
    - echo $CI_COMMIT_REF_NAME

job2:
  script:
    - echo "Using protected variable"
    - echo $SECRET_KEY
  only:
    - main
```

## Parallel Jobs

```yaml
# .gitlab-ci.yml
test:
  stage: test
  parallel:
    matrix:
      - NODE_VERSION: ['16', '18', '20']
  image: node:${NODE_VERSION}
  script:
    - npm ci
    - npm test
```
