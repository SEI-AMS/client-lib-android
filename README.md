# client-lib-android

A Cloudlet-Ready app is an application that is able to use a service running on a Cloudlet. To the app, this service is composed mainly of an IP adresss and port on which a server for the app is listening. A cloudlet-ready app uses the Cloudlet Android Library (client-lib-android) to explicitly look for a service on a nearby Cloudlet, and get an IP and port to connect to.

The easiest way to test changes to this library in cloudlet-client or a cloudlet-ready app, is to use the "gradlew install" command to install the changed library to a local Maven repo. This requires installing Maven first (https://maven.apache.org/download.cgi), in order to have the local .m2 repo in the system.

Cloudlets are discoverable, generic, stateless servers located in single-hop proximity of mobile devices, that can operate in disconnected mode and are virtual-machine (VM) based to promote flexibility, mobility, scalability, and elasticity. In our implementation of cloudlets, applications are statically partitioned into a very thin client that runs on the mobile device and a computation-intensive Server that runs inside a Service VM. Read more about cloudlets at http://sei.cmu.edu/mobilecomputing/research/tactical-cloudlets/.

KD-Cloudlet comprises a total of 7 GitHub projects:

* pycloud (Cloudlet Server)

* cloudlet-client (Cloudlet Client)

* client-lib-android (Library for Cloudlet-Ready Apps)

* client-lib (Java REST Client Library)

* android-logger (SLF4J Logger for Android)

* speech-server (Test server: Speech Recognition Server based on Sphinx)

* speech-android (Test client: Speech Recognition Client)

Building and Installation information in https://github.com/SEI-AMS/pycloud/wiki.
