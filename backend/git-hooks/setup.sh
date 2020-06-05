#!/bin/sh

if ! git remote get-url origin | grep "lab.si.usi.ch" >/dev/null 2>/dev/null; then
  echo "Not in the project!"
  echo "Call this script while in the root directory of the backend project";
  exit 1;
elif ! [ -d "./git-hooks" ]; then
  echo "Not in the right directory!"
  echo "Call this script while in the root directory of the backend project";
  exit 1;
fi;

git config --unset core.hooksPath

this_dir="$(dirname $(realpath "$0"))"
hook_script="$this_dir/pre-commit.sh"
ln -svf "$hook_script" "$this_dir/../.git/hooks/pre-commit"

echo "Commit hook installed"
