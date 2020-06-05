#!/bin/sh

git config --unset core.hooksPath

if [[ -z $(which realpath) ]]; then
    this_dir="$(pwd)/hooks"
else
    this_dir="$(dirname $(realpath "$0"))"
fi

hook_script="$this_dir/pre-commit.sh"
ln -svf "$hook_script" "$this_dir/../.git/hooks/pre-commit"

echo "Commit hook installed"
