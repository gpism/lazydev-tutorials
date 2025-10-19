# Git Workflow

Standard git workflows for teams.

## Feature Branch Workflow

```bash
# Start new feature
git checkout main
git pull origin main
git checkout -b feature/new-feature

# Work on feature
git add .
git commit -m "Add new feature"

# Push feature branch
git push origin feature/new-feature

# After PR is merged, cleanup
git checkout main
git pull origin main
git branch -d feature/new-feature
```

## Hotfix Workflow

```bash
# Create hotfix branch
git checkout main
git checkout -b hotfix/critical-bug

# Fix and test
git add .
git commit -m "Fix critical bug"

# Push and merge
git push origin hotfix/critical-bug

# After merge
git checkout main
git pull origin main
git branch -d hotfix/critical-bug
```

## Commit Message Format

```bash
# Good commit messages
git commit -m "feat: add user authentication"
git commit -m "fix: resolve login timeout issue"
git commit -m "docs: update API documentation"
git commit -m "refactor: simplify database queries"
git commit -m "test: add unit tests for user service"
```

## Squash Commits Before PR

```bash
# Squash last 3 commits
git rebase -i HEAD~3

# In the editor, change 'pick' to 'squash' for commits to merge
# Save and edit the commit message

# Force push (only on feature branches!)
git push --force origin feature/your-branch
```

## Sync Fork with Upstream

```bash
# Add upstream remote (once)
git remote add upstream https://github.com/original-owner/repo.git

# Sync with upstream
git fetch upstream
git checkout main
git merge upstream/main
git push origin main
```
