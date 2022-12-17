#!/bin/sh

## App Service - 高度なツールからの ssh 接続を有効化
apk add openssh-server

echo "root:Docker!" | chpasswd

cat<<EOF >/etc/ssh/sshd_config
Port                    2222
ListenAddress           0.0.0.0
LoginGraceTime          180
X11Forwarding           yes
Ciphers aes128-cbc,3des-cbc,aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr
MACs hmac-sha1,hmac-sha1-96
StrictModes             yes
SyslogFacility          DAEMON
PasswordAuthentication  yes
PermitEmptyPasswords    no
PermitRootLogin         yes
Subsystem sftp internal-sftp
EOF

ssh-keygen -A

if [ ! -d "/var/run/sshd" ]; then
    mkdir -p /var/run/sshd
fi

/usr/sbin/sshd

## アプリケーションを PID 1 で起動する。
exec java -cp @/app/jib-classpath-file @/app/jib-main-class-file
