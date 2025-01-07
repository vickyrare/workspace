import argparse
import subprocess
import sys
import os

def get_changed_files_between_versions(base_revision, target_revision):
    """
    Get the list of files changed in a Git merge request (MR).
    Compares the feature branch with the base branch.
    """
    try:
        # get the current branch name
        result = subprocess.run(
            ["git", "branch", "--show-current", ],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
        )

        # Use `git diff` to get the file names between branches
        result = subprocess.run(
            ["git", "diff", "--name-only", base_revision, target_revision],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
        )
        if result.returncode != 0:
            print(f"Error retrieving MR files: {result.stderr}")
            sys.exit(1)

        # Get the file names from the output
        files = result.stdout.strip().split("\n")
        return [file for file in files if file]  # Remove empty lines
    except Exception as e:
        print(f"An error occurred: {e}")
        sys.exit(1)


def open_files_in_intellij(files):
    """
    Open a list of files in IntelliJ IDEA.
    """
    for file in files:
        # Check if the file exists before opening
        if os.path.exists(file):
            subprocess.run(["idea", file])
        else:
            print(f"File does not exist: {file}")


def main():
    parser = argparse.ArgumentParser(description="-b abc -t def")
    parser.add_argument("-b", "--base_revision", help="Base revision")
    parser.add_argument("-t", "--target_revision", help="Target revision")

    # Parse the arguments
    args = parser.parse_args()

    files = get_changed_files_between_versions(args.base_revision, args.target_revision)

    if not files:
        print("No files were changed in this merge request.")
        sys.exit(0)

    print(f"Opening {len(files)} files in IntelliJ IDEA...")
    open_files_in_intellij(files)


if __name__ == "__main__":
    main()
