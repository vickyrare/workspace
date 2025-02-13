import argparse
import subprocess
import json
import os


def transform_oci_url(input_url):
    try:
        # Parse the input string
        if not input_url.startswith("oci://"):
            raise ValueError("Input must start with 'oci://'")

        # Remove the "oci://" prefix
        stripped_url = input_url[6:]

        # Split the bucket, namespace, and file
        bucket_namespace, file_name = stripped_url.split("/", 1)
        bucket, namespace = bucket_namespace.split("@")

        # Generate the OCI CLI command
        command = [
            "oci", "os", "object", "get",
            "--auth", "security_token",
            "--namespace", namespace,
            "--bucket-name", bucket,
            "--name", file_name,
            "--file", os.path.basename(file_name)
        ]

        return command
    except ValueError as e:
        print(f"Error: {e}")
        return None


def execute_command(command):
    try:
        # Run the command
        result = subprocess.run(command, check=True, text=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        print("Command executed successfully:")
        print(result.stdout)
    except subprocess.CalledProcessError as e:
        print("Command failed with error:")
        print(e.stderr)


def prettify_json_file(input_file, output_file=None):
    try:
        # Open and read the JSON file
        with open(input_file, "r") as file:
            data = json.load(file)

        # Prettify the JSON data
        prettified_json = json.dumps(data, indent=4)

        # Optionally write the prettified JSON to a file
        if output_file:
            with open(output_file, "w") as out_file:
                out_file.write(prettified_json)
            print(f"\nPrettified JSON saved to: {output_file}")

    except FileNotFoundError:
        print(f"Error: File '{input_file}' not found.")
    except json.JSONDecodeError:
        print(f"Error: File '{input_file}' is not valid JSON.")


if __name__ == "__main__":
    # Set up argument parser
    parser = argparse.ArgumentParser(
        description="Transform OCI URL into OCI CLI command, execute it")
    parser.add_argument("input_url", help="OCI URL in the format 'oci://bucket@namespace/file'")

    args = parser.parse_args()

    output_file = os.path.basename(args.input_url)

    # Transform the input
    cli_command = transform_oci_url(args.input_url)

    if cli_command:
        # Print the command (optional)
        print("Generated Command:")
        print(" ".join(cli_command))

        # Execute the command
        execute_command(cli_command)
        if output_file.endswith(".json"):
            prettify_json_file(output_file, output_file)

