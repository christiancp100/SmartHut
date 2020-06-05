#!/bin/sh

FILES=$(git diff --cached --name-only --diff-filter=ACMR | sed 's| |\\ |g')

# Prettify all selected files
cd $(git rev-parse --show-toplevel)/smart-hut
npm run eslint-fix
cd ..

# Add back the modified/prettified files to staging
echo "$FILES" | xargs git add

exit 0
