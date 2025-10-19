# Git Common Commands

Quick reference for everyday git commands.

## Initialize a Repo

```bash
git init
git add .
git commit -m "Initial commit"
```

## Clone a Repository

```bash
git clone https://github.com/username/repo.git
cd repo
```

## Basic Workflow

```bash
# Check status
git status

# Add files
git add .                    # Add all files
git add file.txt             # Add specific file

# Commit
git commit -m "Your message"

# Push
git push origin main
```

## Branch Management

```bash
# Create and switch to new branch
git checkout -b feature-name

# Switch branches
git checkout main

# List branches
git branch -a

# Delete branch
git branch -d feature-name
```

## Undo Changes

```bash
# Discard local changes
git checkout -- file.txt

# Unstage file
git reset HEAD file.txt

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes)
git reset --hard HEAD~1
```

## Remote Management

```bash
# View remotes
git remote -v

# Add remote
git remote add origin https://github.com/username/repo.git

# Change remote URL
git remote set-url origin https://github.com/username/new-repo.git
```

## Stash Changes

```bash
# Save changes temporarily
git stash

# List stashes
git stash list

# Apply latest stash
git stash pop

# Apply specific stash
git stash apply stash@{0}
```

## View History

```bash
# Show commit log
git log

# Show compact log
git log --oneline

# Show changes in commit
git show <commit-hash>

# Show file history
git log --follow file.txt
```

## Sync with Remote

```bash
# Fetch latest changes
git fetch origin

# Pull changes
git pull origin main

# Pull with rebase
git pull --rebase origin main
```
