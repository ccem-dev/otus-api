package br.org.otus.gateway;

public class MicroservicesResources{
    protected String HOST;
    public String PORT;
    private MicroservicesEnvironments microservicesEnvironments;

    public MicroservicesResources(MicroservicesEnvironments microservicesEnvironments){
        this.microservicesEnvironments = microservicesEnvironments;
        readAddress();
    }

    private void readAddress(){
        //HOST = System.getenv(microservicesEnvironments.getHost());
        //PORT = System.getenv(microservicesEnvironments.getPort());

        HOST = "http://localhost:";
        PORT = "8083";
    }

}
