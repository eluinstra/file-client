# Secure File Client
With the Secure FileClient users can up files to and download files from a Secure FileServer using their SSL client certificates for authentication. The FileClient is managed through a SOAP interface. Files will be downloaded over HTTPS using HTTP GET (Ranges are supported). Files will be uploaded using the tus protocol. The FileClient can also be used for Grote Berichten file transfer.  

See https://github.com/eluinstra/file-server for the FileServer.