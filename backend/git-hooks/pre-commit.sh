#!/bin/sh

set -e

echo "Java formatter running..."

format_cmd="$(dirname $(realpath "$0"))/format.sh"

# skip if NO_VERIFY env var set
if [ "$NO_VERIFY" ]; then
    echo 'google-java-format skipped' 1>&2
    exit 0
fi

# list all added/copied/modified/renamed java files
files="`git diff --staged --name-only --diff-filter=ACMR | egrep -a '.java$' | tr \"\\n\" \" \"`"
for f in $files; do
	$format_cmd --aosp -i "$f"
	git add -f "$f"
done
