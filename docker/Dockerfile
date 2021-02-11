FROM jakartaee/cts-javamail-base:0.1

WORKDIR /root
RUN wget 'https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz' -O openjdk-11.tar.gz -q
RUN tar -xzf openjdk-11.tar.gz

ENV JAVA_HOME '/root/jdk-11'

ENTRYPOINT cat