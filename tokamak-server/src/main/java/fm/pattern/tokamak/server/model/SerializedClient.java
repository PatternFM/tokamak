package fm.pattern.tokamak.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "SerializedClients")
public class SerializedClient {

	@Getter
	@Setter
	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@Getter
	@Setter
	@Column(name = "client_id", nullable = false)
	private String clientId;

	@Getter
	@Setter
	@Column(name = "created", nullable = false)
	private Date created;

	@Getter
	@Setter
	@Column(name = "payload", nullable = false)
	private String payload;

	public SerializedClient() {

	}

	public SerializedClient(String id, String clientId, Date created, String payload) {
		this.id = id;
		this.clientId = clientId;
		this.created = created;
		this.payload = payload;
	}

}
