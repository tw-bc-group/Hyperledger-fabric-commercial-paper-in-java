#!/bin/sh
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
export PATH=$GOPATH/src/github.com/hyperledger/fabric/build/bin:${PWD}/../bin:${PWD}:$PATH
export FABRIC_CFG_PATH=${PWD}
CHANNEL_NAME=mychannel

# remove previous crypto material and config transactions
rm -fr ../commercialpaper-app/src/main/resources/config/*
rm -fr ../commercialpaper-app/src/main/resources/crypto-config/*

# generate crypto material
cryptogen generate --config=./crypto-config.yaml --output ../commercialpaper-app/src/main/resources/crypto-config
if [ "$?" -ne 0 ]; then
  echo "Failed to generate crypto material..."
  exit 1
fi

# generate genesis block for orderer
configtxgen -profile TwoOrgsOrdererGenesis -outputBlock ../commercialpaper-app/src/main/resources/config/genesis.block
if [ "$?" -ne 0 ]; then
  echo "Failed to generate orderer genesis block..."
  exit 1
fi

# generate channel configuration transaction
configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ../commercialpaper-app/src/main/resources/config/channel.tx -channelID $CHANNEL_NAME
if [ "$?" -ne 0 ]; then
  echo "Failed to generate channel configuration transaction..."
  exit 1
fi

# generate anchor peer transaction
#configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ../commercialpaper-app/src/main/resources/config/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP
#if [ "$?" -ne 0 ]; then
#  echo "Failed to generate anchor peer update for Org1MSP..."
#  exit 1
#fi
