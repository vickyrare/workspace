import oci
import requests
import json
import argparse
import base64
import datetime
import os

# --- Helper Functions ---
def get_secret_bundle(secret_id):
    """
    Retrieves the latest secret bundle content.
    """
    try:
        get_secret_bundle_response = secrets_client.get_secret_bundle(secret_id)
        secret_content_base64 = get_secret_bundle_response.data.secret_bundle_content.content
        secret_content = base64.b64decode(secret_content_base64).decode('utf-8')
        try:
            # Assume the secret is a JSON object containing the token
            return json.loads(secret_content)
        except json.JSONDecodeError:
            # If not JSON, assume the secret content is the token itself.
            # Return it in a dictionary to match the expected format.
            return {"access_token": secret_content}
    except oci.exceptions.ServiceError as e:
        if e.status == 404:
            return None # Secret not found
        print(f"Error retrieving secret bundle: {e}")
        return None

def create_or_update_secret(secret_name, compartment_id, vault_id, token_json, username, artifactory_url, key_id):
    """
    Creates a new secret or updates an existing one with a new version using VaultsClient.
    """
    secret_content = json.dumps(token_json)
    secret_content_base64 = base64.b64encode(secret_content.encode('utf-8')).decode('utf-8')

    # Define metadata
    metadata = {
        "username": username,
        "artifactory_url": artifactory_url
    }

    # Check if secret exists using VaultsClient.list_secrets
    secret_exists = False
    existing_secret_id = None
    try:
        list_secrets_response = vaults_client.list_secrets(
            compartment_id=compartment_id,
            name=secret_name,
            vault_id=vault_id
        )
        if list_secrets_response.data: # Check if the list is not empty
            existing_secret_id = list_secrets_response.data[0].id # Access the first item in the list
            secret_exists = True
    except oci.exceptions.ServiceError as e:
        print(f"Error checking for existing secret with VaultsClient: {e}")
        return None # Or re-raise, depending on desired error handling

    if secret_exists:
        # Secret exists, create a new version
        secret_id = existing_secret_id
        print(f"Secret '{secret_name}' found. Creating new version...")

        # Update description
        description = (
            f"Artifactory token for user '{username}' on instance '{artifactory_url}'. "
            f"Last updated: {datetime.datetime.now().isoformat()}"
        )
        
        # Update secret using VaultsClient
        update_secret_response = vaults_client.update_secret(
            secret_id=secret_id,
            update_secret_details=oci.vault.models.UpdateSecretDetails(
                description=description,
                freeform_tags=metadata,
                secret_content=oci.vault.models.Base64SecretContentDetails(
                    content=secret_content_base64,
                    content_type="BASE64"
                )
            )
        )
        print(f"New version created for secret '{secret_name}'. Secret OCID: {update_secret_response.data.id}")
        return update_secret_response.data.id
    else:
        # Secret does not exist, create it
        print(f"Secret '{secret_name}' not found. Creating new secret...")
        description = (
            f"artifactory-'{username}'-'{artifactory_url}' "
            f"Created: {datetime.datetime.now().isoformat()}"
        )
        
        # Create secret using VaultsClient
        create_secret_response = vaults_client.create_secret(
            create_secret_details=oci.vault.models.CreateSecretDetails(
                compartment_id=compartment_id,
                secret_name=secret_name,
                vault_id=vault_id,
                key_id=key_id, # Required for creating a secret
                secret_content=oci.vault.models.Base64SecretContentDetails(
                    content=secret_content_base64,
                    content_type="BASE64"
                ),
                description=description,
                freeform_tags=metadata
            )
        )
        print(f"Secret '{secret_name}' created. Secret OCID: {create_secret_response.data.id}")
        return create_secret_response.data.id

def output_token(token_json, output_format, username=None):
    """
    Outputs the access token in various formats.
    """
    access_token = token_json.get("access_token")
    if not access_token:
        print("Error: Access token not found in the response.")
        return

    if output_format == "value":
        print(access_token)
    elif output_format == "username_token":
        if username:
            print(f"{username}/{access_token}")
        else:
            print("Error: Username not provided for 'username_token' format.")
    elif output_format == "json":
        output_data = {
            "username": username if username else "artifactory_user", # Placeholder if username not available
            "password": access_token
        }
        print(json.dumps(output_data, indent=2))
    else:
        print(f"Unsupported output format: {output_format}")

def refresh_artifactory_token(artifactory_url, refresh_token, access_token):
    """
    Refreshes an access token from Artifactory using a refresh token.
    Optionally uses an existing access token for authorization.
    """
    token_url = f"{artifactory_url}/artifactory/api/security/token"
    if 'csartifactorydev' in token_url:
        #cs dev artifactory have a different end-point to refresh access token based on aud
        token_url = f"{artifactory_url}/access/api/v1/tokens"
    data = {
        "grant_type": "refresh_token",
        "expires_in": 7776000,
        "access_token": access_token,
        "refresh_token": refresh_token
    }
    try:
        response = requests.post(token_url, data=data)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error refreshing Artifactory token: {e}")
        return None

def ping_artifactory(secret_id, artifactory_url_arg=None):
    """
    Tests the validity of the Artifactory token stored in a secret by hitting the /ping endpoint.
    """
    secret_bundle = get_secret_bundle(secret_id)
    if not secret_bundle:
        print(f"Error: Could not retrieve secret content for ID: {secret_id}")
        return False

    access_token = secret_bundle.get("access_token")
    if not access_token:
        print("Error: Access token not found in the secret content.")
        return False

    artifactory_url = artifactory_url_arg # Use provided argument first
    if not artifactory_url:
        # Retrieve Artifactory URL from secret metadata (freeform_tags)
        # Workaround: If get_secret is not available, we cannot retrieve artifactory_url from metadata.
        # We will print an error and ask the user to provide it via --artifactory-url.
        print("Warning: 'secrets_client.get_secret' method is not available in your OCI SDK version.")
        print("Cannot retrieve 'artifactory_url' from secret metadata.")
        print("Please provide the Artifactory URL using the '--artifactory-url' argument.")
        return False

    if not artifactory_url: # Check again if it's still not set
        print("Error: Artifactory URL is not available. Please provide it using the '--artifactory-url' argument.")
        return False

    ping_url = f"{artifactory_url}/artifactory/api/system/ping"
    headers = {"Authorization": f"Bearer {access_token}"}

    try:
        response = requests.get(ping_url, headers=headers)
        response.raise_for_status()
        if response.text.strip() == "OK":
            print(f"Artifactory ping successful for secret ID {secret_id}!")
            return True
        else:
            print(f"Artifactory ping failed for secret ID {secret_id}. Response: {response.text}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"Error during Artifactory ping for secret ID {secret_id}: {e}")
        return False

# Global clients
secrets_client = None
vaults_client = None

def main():
    parser = argparse.ArgumentParser(description="Manage Artifactory access tokens in OCI Vault.")
    subparsers = parser.add_subparsers(dest="command", help="Available commands")

    # Fetch command (removed as username/password authentication is removed)

    # Store command (create/update secret)
    store_parser = subparsers.add_parser("store", help="Refresh an Artifactory token and store/update it in OCI Vault.")
    store_parser.add_argument("--profile", default="DEFAULT", help="The config profile to use from ~/.oci/config.")
    store_parser.add_argument("--source-secret-id", required=True, help="OCID of the secret containing the refresh token.")
    store_parser.add_argument("--artifactory-url", required=True, help="Base URL of the Artifactory instance.")
    store_parser.add_argument("--username", help="Artifactory username. Used for metadata.")
    store_parser.add_argument("--compartment-id", required=True, help="OCID of the compartment where the secret will reside.")
    store_parser.add_argument("--vault-id", required=True, help="OCID of the Vault where the secret will reside.")
    store_parser.add_argument("--secret-name", required=True, help="Name of the secret. Should follow 'artifactory-<instance>-<username>' convention.")
    store_parser.add_argument("--key-id", required=True, help="OCID of the Key to encrypt the secret.")

    # Ping command
    ping_parser = subparsers.add_parser("ping", help="Test the validity of an Artifactory token stored in a secret.")
    ping_parser.add_argument("--profile", default="DEFAULT", help="The config profile to use from ~/.oci/config.")
    ping_parser.add_argument("--secret-id", required=True, help="OCID of the secret containing the Artifactory token.")
    ping_parser.add_argument("--artifactory-url", help="Base URL of the Artifactory instance. If not provided, it will be retrieved from the secret's metadata.")

    args = parser.parse_args()
    print(f"Parsed arguments: {args}")

    # OCI SDK Setup - moved here to use --profile argument
    global secrets_client, vaults_client

    try:
        config = oci.config.from_file(profile_name=args.profile)
        print(f"Using OCI config profile: {args.profile}")
    except Exception as config_e:
        print(f"Error loading OCI config file with profile '{args.profile}': {config_e}")
        print("Please ensure your OCI config file (~/.oci/config) is correctly set up or provide valid credentials.")
        exit(1)

    signer = None
    # Check for security token session and create a signer if found
    token_file = os.path.expanduser(os.path.join('~', '.oci', 'sessions', 'DEFAULT', 'token'))
    if os.path.exists(token_file):
        print("Found security token session, attempting to use it.")
        try:
            from oci.auth.signers import SecurityTokenSigner
            from oci.signer import load_private_key_from_file

            key_file = config.get('key_file')
            if not key_file:
                raise Exception("'key_file' not found in OCI config. It's required for security token authentication.")

            with open(token_file, 'r') as f:
                token = f.read()

            private_key = load_private_key_from_file(os.path.expanduser(key_file), config.get('pass_phrase'))
            signer = SecurityTokenSigner(token, private_key)
        except Exception as e:
            print(f"Could not create security token signer: {e}. Falling back to API key authentication.")
            signer = None

    try:
        if signer:
            # When providing a signer, we still need to provide the region.
            region = config.get('region')
            if not region:
                raise Exception("Region not found in OCI config and is required.")

            client_config = {'region': region}
            secrets_client = oci.secrets.secrets_client.SecretsClient(config=client_config, signer=signer)
            vaults_client = oci.vault.vaults_client.VaultsClient(config=client_config, signer=signer)
        else:
            print("Using API Key authentication from config file.")
            secrets_client = oci.secrets.secrets_client.SecretsClient(config)
            vaults_client = oci.vault.vaults_client.VaultsClient(config)
    except oci.exceptions.InvalidConfig as e:
        print(f"OCI Config is invalid: {e}")
        print("Please ensure your OCI config file (~/.oci/config) is correctly set up.")
        exit(1)
    except Exception as e:
        print(f"Error creating OCI clients: {e}")
        exit(1)

    if args.command == "store":
        # 1. Read the existing secret to get the refresh token and access token
        existing_secret_content = get_secret_bundle(args.source_secret_id)
        if not existing_secret_content:
            print(f"Error: Could not retrieve existing secret content for ID: {args.source_secret_id}")
            exit(1)
        
        refresh_token = existing_secret_content.get("refresh_token")
        if not refresh_token:
            print(f"Error: 'refresh_token' not found in secret content for ID: {args.source_secret_id}")
            exit(1)

        access_token = existing_secret_content.get("access_token")
        if not access_token:
            print(f"Warning: 'access_token' not found in secret content for ID: {args.source_secret_id}. Proceeding without it for authorization.")

        # 2. Refresh the access token
        token_json = refresh_artifactory_token(args.artifactory_url, refresh_token, access_token)
        if not token_json:
            print("Error: Failed to refresh Artifactory token.")
            exit(1)

        # 3. Store the new token information back into the secret
        # For metadata, we still need username and artifactory_url.
        # If not provided, we'll use placeholders or raise an error if strictly needed.
        if not args.username:
            print("Warning: --username not provided. Using 'unknown_user' for metadata.")
            args.username = "unknown_user"
        
        create_or_update_secret(args.secret_name, args.compartment_id, args.vault_id, token_json, args.username, args.artifactory_url, args.key_id)
    elif args.command == "ping":
        ping_artifactory(args.secret_id, args.artifactory_url)
    else:
        parser.print_help()

if __name__ == "__main__":
    main()
