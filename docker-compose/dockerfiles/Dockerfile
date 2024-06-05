# Use the specified base image
FROM mnykolaichuk/configserver:v1

# Install openssh-client to get ssh-keyscan
RUN apt-get update && apt-get install -y openssh-client

# Copy the SSH private key into the container
COPY id_rsa.pub /root/.ssh/id_rsa.pub
COPY id_rsa /root/.ssh/id_rsa

# Ensure the known_hosts file exists
RUN touch /root/.ssh/known_hosts

# Add GitHub to the known hosts to avoid SSH prompt
RUN ssh-keyscan github.com >> /root/.ssh/known_hosts

# Set the correct permissions for the SSH private key and SSH public key
RUN chmod 600 /root/.ssh/id_rsa
RUN chmod 644 /root/.ssh/id_rsa.pub

#docker build -t mnykolaichuk/configserver:v2 .

# Po zbudowaniu kluczy można usunac w celach bezpieczenstwa