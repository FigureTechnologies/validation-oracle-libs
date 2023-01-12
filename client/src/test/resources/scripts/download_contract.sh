#!/bin/sh

### Update repositories
apt-get -y update > /dev/null

### Install necessary utilities
apt-get -y install git > /dev/null || exit 1

### Clone the source code of the smart contract
git clone -b "${SMART_CONTRACT_VERSION_TAG:-main}" \
    https://github.com/FigureTechnologies/validation-oracle-smart-contract/ || exit 1

### Copy the smart contract files to the directory expected by the optimizer container
cp -r validation-oracle-smart-contract/* /code || exit 1

### Indicate to other containers that the contract has been downloaded
touch /outputs/contractDownloadCompletionIndicator
echo "Finished downloading the contract!"
