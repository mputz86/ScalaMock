
# Setup for publishing to cich.innoq.com

## Adding Server Certificate to Java Keystore [OSX]

- Download server certificate from Nexus server

    cd ~/Desktop
    # does not work for me, so I used the browser
    openssl s_client -connect cich.innoq.io:8081 -showcerts

- backup java keystore and add server certificate

    cd /Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/jre/lib/security
    sudo cp cacerts cacerts.bak
    sudo keytool -keystore cacerts -importcert -alias cich -file ~/Desktop/cich.innoq.io.cer                                                                                                                            13:44:51  ☁  origin-master ☂ 𝝙 ⚡ ✭

- create credentials for Nexus server file in ~/.ivy2/.credentials

    cd ~/.ivy2
    echo "realm=Sonatype Nexus
    host=cich.innoq.io
    user=your-username
    password=your-password" > .credentials
