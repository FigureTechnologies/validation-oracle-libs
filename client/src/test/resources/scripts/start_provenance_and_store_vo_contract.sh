#!/bin/sh

### Copy the blockchain initialization files from the test folder to the Docker volume
cp -rn /provenance_seed/* /provenance

### Start the Provenance instance
echo "Starting Provenance..."
#/usr/bin/provenanced -t --home /provenance start &             # Use this to be able to see the Provenance node's logs
/usr/bin/provenanced -t --home /provenance start > /dev/null 2>&1 & # Use this to hide the Provenance node logs

### Wait for the optimized contract to exist
echo "Waiting for the optimized contract WASM to be created..."
while [ ! -f /outputs/contractOptimizationCompletionIndicator ]
  do sleep .1
done
echo "The optimized contract WASM was found! Beginning contract setup..."

### Change directories to the Provenance directory in order to invoke the Provenance container's provenanced binary
cd /provenance || exit 1

PROVENANCED="/usr/bin/provenanced"

### Wait for Provenance to fully stand up, just to be safe
sleep 5

# TODO: Investigate getting accounts pre-initialized in the seed?
### Create an address which will act as the administrator of the smart contract
echo "$CONTRACT_ADMIN_MNEMONIC" | $PROVENANCED keys add marketplace-admin --home /provenance --keyring-backend test \
    --testnet --hd-path "44'/1'/0'/0/0" --recover  --output json | jq || exit 1
ADMIN_ACCOUNT="$($PROVENANCED keys show -a marketplace-admin --home /provenance --keyring-backend test -t)" || exit 1

### Create an address which will act as a participant in the smart contract
echo "$PARTY1_MNEMONIC" | $PROVENANCED keys add party1 --home /provenance --keyring-backend test \
    --testnet --hd-path "44'/1'/0'/0/0" --recover  --output json | jq || exit 1
PARTY1_ACCOUNT="$($PROVENANCED keys show -a party1 --home /provenance --keyring-backend test -t)" || exit 1

### Create an address which will act as another participant in the smart contract
echo "$PARTY2_MNEMONIC" | $PROVENANCED keys add party2 --home /provenance --keyring-backend test \
    --testnet --hd-path "44'/1'/0'/0/0" --recover  --output json | jq || exit 1
PARTY2_ACCOUNT="$($PROVENANCED keys show -a party2 --home /provenance --keyring-backend test -t)" || exit 1

### Create an address which will act as yet another participant in the smart contract
echo "$PARTY3_MNEMONIC" | $PROVENANCED keys add party3 --home /provenance --keyring-backend test \
    --testnet --hd-path "44'/1'/0'/0/0" --recover  --output json | jq || exit 1
PARTY3_ACCOUNT="$($PROVENANCED keys show -a party3 --home /provenance --keyring-backend test -t)" || exit 1

### Define the address of a validator node in order to fund addresses
VALIDATOR="$($PROVENANCED keys show -a validator --home /provenance --keyring-backend test -t)" || exit 1

### Create the administrator account
$PROVENANCED tx bank send \
    "$VALIDATOR" \
    "$ADMIN_ACCOUNT" \
    350000000000nhash \
    --from="$VALIDATOR" \
    --keyring-backend=test \
    --home=/provenance \
    --chain-id=chain-local \
    --gas=auto \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.5 \
    --broadcast-mode=block \
    --yes \
    --testnet \
    --output json | jq || exit 1

### Create the party1 account
$PROVENANCED tx bank send \
    "$VALIDATOR" \
    "$PARTY1_ACCOUNT" \
    350000000000nhash \
    --from="$VALIDATOR" \
    --keyring-backend=test \
    --home=/provenance \
    --chain-id=chain-local \
    --gas=auto \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.5 \
    --broadcast-mode=block \
    --yes \
    --testnet \
    --output json | jq || exit 1

### Create the party2 account
$PROVENANCED tx bank send \
    "$VALIDATOR" \
    "$PARTY2_ACCOUNT" \
    350000000000nhash \
    --from="$VALIDATOR" \
    --keyring-backend=test \
    --home=/provenance \
    --chain-id=chain-local \
    --gas=auto \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.5 \
    --broadcast-mode=block \
    --yes \
    --testnet \
    --output json | jq || exit 1

### Create the party3 account
$PROVENANCED tx bank send \
    "$VALIDATOR" \
    "$PARTY3_ACCOUNT" \
    350000000000nhash \
    --from="$VALIDATOR" \
    --keyring-backend=test \
    --home=/provenance \
    --chain-id=chain-local \
    --gas=auto \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.5 \
    --broadcast-mode=block \
    --yes \
    --testnet \
    --output json | jq || exit 1

### Create an unrestricted name that we will bind the address of the smart contract to
$PROVENANCED tx name bind \
    "sc" \
    "$VALIDATOR" \
    "pb" \
    --restrict=false \
    --from "$VALIDATOR" \
    --keyring-backend test \
    --home /provenance \
    --chain-id chain-local \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.5 \
    --broadcast-mode block \
    --yes \
    --testnet \
    --output json | jq || exit 1

### Store the optimized contract WASM file to the chain
PATH_TO_CONTRACT="/code/artifacts/validation_oracle_smart_contract.wasm"
### Note: Will need to use --instantiate-anyof-addresses instead of --instantiate-only-address in newer Provenance versions
WASM_STORE=$($PROVENANCED tx wasm store "$PATH_TO_CONTRACT" \
    --instantiate-only-address "$ADMIN_ACCOUNT" \
    --from "$ADMIN_ACCOUNT" \
    --keyring-backend test \
    --home /provenance \
    --chain-id chain-local \
    --gas auto \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.2 \
    --broadcast-mode block \
    --yes \
    --testnet \
    --output json | jq || exit 1)

echo "$WASM_STORE"

## Note the value of the code_id for our contract from the above output
VO_CODE_ID=$(echo "$WASM_STORE" | jq -r '.logs[] | select(.msg_index == 0) | .events[] | select(.type == "store_code") | .attributes[] | select(.key == "code_id") | .value')

echo "VO Code ID is $VO_CODE_ID"

### Instantiate the contract
$PROVENANCED tx wasm instantiate "$VO_CODE_ID" \
    '{ "contract_name": "Validation Oracle - Local demo for integration testing", "bind_name": "vo.sc.pb", "create_request_nhash_fee": "3000" }' \
    --admin "$ADMIN_ACCOUNT" \
    --label validation-oracle-test-demo \
    --from marketplace-admin \
    --keyring-backend test \
    --home /provenance \
    --chain-id chain-local \
    --gas auto \
    --gas-prices="1905nhash" \
    --gas-adjustment=1.5 \
    --broadcast-mode block \
    --yes \
    --testnet \
    --output json | jq || exit 1

### Store the address of the contract for convenience
#### The jq command will need to be refined if more than one address is returned from the previous command
VO_CONTRACT=$($PROVENANCED query wasm list-contract-by-code "$VO_CODE_ID" -t -o json | jq -r '.contracts[0]')

if [ -z "$VO_CONTRACT" ]
then
  echo "The contract was not successfully instantiated to the local Provenance instance. Check the logs above for an error."
  exit 1
else
  echo "The contract was successfully instantiated to the local Provenance instance at address $VO_CONTRACT"
fi

### Export the addresses
export ADMIN_ACCOUNT
export PARTY1_ACCOUNT
export PARTY2_ACCOUNT
export PARTY3_ACCOUNT
export VALIDATOR
export VO_CONTRACT

### Stall so that the integration tests may run
rm -f someRandomFileThatDoesNotActuallyExist
tail -F someRandomFileThatDoesNotActuallyExist > /dev/null 2>&1
