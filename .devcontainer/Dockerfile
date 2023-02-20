# ベースイメージ情報: https://github.com/microsoft/vscode-dev-containers/blob/main/containers/java/history/0.205.3.md

# [Choice] Java version (use -bullseye variants on local arm64/Apple Silicon): 11, 17, 11-bullseye, 17-bullseye, 11-buster, 17-buster
ARG VARIANT="17-bullseye"
# (d1)
FROM mcr.microsoft.com/vscode/devcontainers/java:0.205.3-${VARIANT}

# (d2)
# [Option] Install Maven
ARG INSTALL_MAVEN="false"
ARG MAVEN_VERSION=""
# [Option] Install Gradle
ARG INSTALL_GRADLE="false"
ARG GRADLE_VERSION=""
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi \
    && if [ "${INSTALL_GRADLE}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; fi

# (d3)
# [Choice] Node.js version: none, lts/*, 16, 14, 12, 10
ARG NODE_VERSION="none"
RUN if [ "${NODE_VERSION}" != "none" ]; then su vscode -c "umask 0002 && . /usr/local/share/nvm/nvm.sh && nvm install ${NODE_VERSION} 2>&1"; fi

# fix: https://github.com/gihyo-book/azure-container-dev-book/pull/17
RUN wget -qO - https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add - \
    && echo deb https://dl.yarnpkg.com/debian/ stable main | sudo tee /etc/apt/sources.list.d/yarn.list

# (d4)
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends wget apt-transport-https gnupg lsb-release \
    && wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo apt-key add - \
    && echo deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main | sudo tee -a /etc/apt/sources.list.d/trivy.list \
    && sudo apt-get update \
    && sudo apt-get install trivy

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1
