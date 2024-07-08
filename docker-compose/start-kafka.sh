#!/bin/bash
echo "Keystore Password: $(cat /etc/kafka/secrets/kafka_server_keystore_password)"
echo "Key Password: $(cat /etc/kafka/secrets/kafka_server_keystore_password)"
echo "Truststore Password: $(cat /etc/kafka/secrets/kafka_server_keystore_password)"
export KAFKA_SSL_KEYSTORE_PASSWORD=$(cat /etc/kafka/secrets/kafka_server_keystore_password)
export KAFKA_SSL_KEY_PASSWORD=$(cat /etc/kafka/secrets/kafka_server_keystore_password)
export KAFKA_SSL_TRUSTSTORE_PASSWORD=$(cat /etc/kafka/secrets/kafka_server_keystore_password)
/etc/confluent/docker/run
