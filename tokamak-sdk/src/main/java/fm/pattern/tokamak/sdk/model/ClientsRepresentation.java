package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

public class ClientsRepresentation {

	private List<ClientRepresentation> clients = new ArrayList<>();

	public ClientsRepresentation() {

	}

	public ClientsRepresentation(List<ClientRepresentation> clients) {
		this.clients = clients;
	}

	public List<ClientRepresentation> getClients() {
		return clients;
	}

	public void setClients(List<ClientRepresentation> clients) {
		this.clients = clients;
	}

}
