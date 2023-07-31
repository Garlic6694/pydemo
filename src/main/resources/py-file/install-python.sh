#!/bin/bash
set -e

# Set the path to the Anaconda3 installer
anaconda_installer="Anaconda3-2020.02-Linux-x86_64.sh"

# Check if the installer file exists
if [ ! -f "$anaconda_installer" ]; then
    echo "Error: Anaconda3 installer not found!"
    exit 1
fi

# Install Anaconda3 in /opt/anaconda3
bash "$anaconda_installer" -b -p "/opt/anaconda3"

# Initialize conda in the current shell
source "/opt/anaconda3/etc/profile.d/conda.sh"
conda init "$(basename "$SHELL")"

# Print success message
echo 'Successfully installed Anaconda3...'

# Display Conda version
echo -n 'Conda version: '
conda --version
echo -e '\n'

# Start a new shell session with conda activated
exec "$(basename "$SHELL")"
