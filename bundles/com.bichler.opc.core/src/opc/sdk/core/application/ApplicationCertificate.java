package opc.sdk.core.application;

import java.util.List;

import org.jdom.Content;
import org.jdom.Element;

public class ApplicationCertificate {
	private String storePath = "";
	private String subjectName = "";
	private String certKeyName = "";

	@SuppressWarnings("unchecked")
	ApplicationCertificate(Content content) {
		for (Content c : ((List<Content>) ((Element) content).getContent())) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ApplicationConfigurationTags.StorePath.name()))
					this.storePath = ((Content) ((Element) c).getContent().get(0)).getValue();
				else if (((Element) c).getName().equals(ApplicationConfigurationTags.SubjectName.name()))
					this.subjectName = ((Content) ((Element) c).getContent().get(0)).getValue();
				else if (((Element) c).getName().equals(ApplicationConfigurationTags.CertKeyName.name()))
					this.certKeyName = ((Content) ((Element) c).getContent().get(0)).getValue();
			}
		}
	}

	/**
	 * Creates an empty application certificate object.
	 *
	 */
	public ApplicationCertificate() {
	}

	public String getStorePath() {
		return storePath;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public String getCertKeyName() {
		return this.certKeyName;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public void setCertKeyName(String certKeyName) {
		this.certKeyName = certKeyName;
	}
}
