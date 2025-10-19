# Contributing to lazydev-tutorials

Thanks for considering contributing! We love copy-pastable code.

## Guidelines

### Keep it Simple
- **Copy-pastable first**: Code should work out of the box
- **Minimal explanations**: Brief comments, not documentation
- **Working examples**: Test your code before submitting

### What to Contribute

Good contributions:
- ✅ Common commands/patterns you use often
- ✅ Updated alternatives to outdated official docs
- ✅ Quick setup guides for popular tools
- ✅ Real-world examples that actually work

Not needed:
- ❌ Detailed explanations (link to docs instead)
- ❌ Theoretical concepts
- ❌ Overly complex examples

## Tutorial Format

Use this structure:

```markdown
# Tutorial Title

One-line description.

## Section Name

[copy-pastable code block]

## Another Section

[more code]
```

## How to Contribute

1. Fork the repo
2. Create a new file or edit an existing one
3. Test your code
4. Submit a PR

That's it! No complicated process.

## Categories

Add tutorials to these directories:
- `/git` - Git commands and workflows
- `/docker` - Docker and containerization
- `/nodejs` - Node.js and JavaScript
- `/python` - Python development
- `/database` - SQL databases
- `/cicd` - CI/CD pipelines

Need a new category? Just create it!

## Style

- Use code blocks with syntax highlighting
- Keep commands Unix-style (bash/sh)
- Add Windows alternatives if needed
- Use comments sparingly
- Real examples > placeholders

## Example Contribution

Good:
```bash
# Start PostgreSQL with Docker
docker run -d \
  --name postgres \
  -e POSTGRES_PASSWORD=secret \
  -p 5432:5432 \
  postgres:15
```

Avoid:
```bash
# This command starts a PostgreSQL database container using Docker.
# The -d flag runs it in detached mode, which means it runs in the 
# background. The --name flag gives it a name so you can reference it
# later. The -e flag sets environment variables...
```

## Questions?

Open an issue or submit a PR anyway. We're not strict about it.

## License

By contributing, you agree your contributions will be licensed under MIT.
