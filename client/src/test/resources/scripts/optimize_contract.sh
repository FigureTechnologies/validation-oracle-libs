#!/bin/sh

### Wait for the contract source code to finish downloading
echo "Waiting for the contract source code to download..."
while [ ! -f /outputs/contractDownloadCompletionIndicator ]
  do sleep .1
done
echo "The contract source code finished downloading! Beginning optimization..."

### Call the default entrypoint of the optimizer
optimize.sh /code || exit 1

### Indicate to other containers that the contract has been optimized
touch /outputs/contractOptimizationCompletionIndicator
