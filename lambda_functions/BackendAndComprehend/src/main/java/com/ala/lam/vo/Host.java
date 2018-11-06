package com.ala.lam.vo;


import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;



public class Host   {
  
  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    HTTP("HTTP"),

        SMTP("SMTP");
    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }

  private TypeEnum type = null;
  private Long id = null;
  private String url = null;
  private HostAddress address = null;
  private Credentials accessSecurity = null;
  private List<ParamValue> httpHeaders=null;
  private AuthSchemeEnum authScheme=null;
  private OAuthInfo oauthInfo=null;
  private NotificationAudit audit=null;
  private Long hostArchId;

  
  

  @JsonIgnore
  private Boolean isHeadersConfigured;
  @JsonIgnore
  private Boolean isAuthConfigured;









  @JsonIgnore
public Boolean isHeadersConfigured() {
	return isHeadersConfigured;
}
  @JsonIgnore
public Long getHostArchId() {
	return hostArchId;
}

public void setHostArchId(Long hostArchId) {
	this.hostArchId = hostArchId;
}

public void setHeadersConfigured(Boolean isHeadersConfigured) {
	this.isHeadersConfigured = isHeadersConfigured;
}
  @JsonIgnore
public Boolean isAuthConfigured() {
	return isAuthConfigured;
}

public void setAuthConfigured(Boolean isAuthConfigured) {
	this.isAuthConfigured = isAuthConfigured;
}

/**
   * Gets or Sets provider
   */
  public enum ProviderEnum {
        SES("SES"),

        SMTP("SMTP"),

        TWILIO("TWILIO"),
        
        GENERICWEBHOOKPROVIDER("GENERICWEBHOOKPROVIDER"),

        HTTPGETPROVIDER("HTTPGETPROVIDER"),

        HTTPPOSTPROVIDER("HTTPPOSTPROVIDER");
    private String value;

    ProviderEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }

  private ProviderEnum provider = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),

        INACTIVE("INACTIVE");
    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }

  private StatusEnum status = null;

  /**
   **/
  
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }
  public void setType(TypeEnum type) {
    this.type = type;
  }

  /**
   * host id value
   **/
  
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }


  


  /**
   * host address url
   **/
  
  @JsonProperty("address")
  public HostAddress getAddress() {
    return address;
  }
  public void setAddress(HostAddress address) {
    this.address = address;
  }

  /**
   **/
  
  @JsonProperty("access_security")
  public Credentials getAccessSecurity() {
    return accessSecurity;
  }
  public void setAccessSecurity(Credentials accessSecurity) {
    this.accessSecurity = accessSecurity;
  }

  /**
   **/
  
  @JsonProperty("provider")
  public ProviderEnum getProvider() {
    return provider;
  }
  public void setProvider(ProviderEnum provider) {
    this.provider = provider;
  }

  /**
   **/
  
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }
  public void setStatus(StatusEnum status) {
    this.status = status;
  }
  

  @JsonProperty("http_headers")
  public List<ParamValue> getHttpHeaders() {
	return httpHeaders;
}
public void setHttpHeaders(List<ParamValue> httpHeaders) {
	this.httpHeaders = httpHeaders;
}

public enum AuthSchemeEnum {
	BASIC("BASIC"),

	BEARER("BEARER"),

	DIGEST("DIGEST");


private String value;

AuthSchemeEnum(String value) {
  this.value = value;
}

@Override
@JsonValue
public String toString() {
  return String.valueOf(value);
}
}


@JsonProperty("auth_scheme")
public AuthSchemeEnum getAuthScheme() {
	return authScheme;
}
public void setAuthScheme(AuthSchemeEnum authScheme) {
	this.authScheme = authScheme;
}
@JsonProperty("oauth_info")
public OAuthInfo getOauthInfo() {
	return oauthInfo;
}
public void setOauthInfo(OAuthInfo oauthInfo) {
	this.oauthInfo = oauthInfo;
}

@JsonProperty("audit")
public NotificationAudit getAudit() {
	return audit;
}
public void setAudit(NotificationAudit audit) {
	this.audit = audit;
}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Host host = (Host) o;
    return Objects.equals(type, host.type) &&
        Objects.equals(id, host.id) &&
        Objects.equals(url, host.url) &&
        Objects.equals(address, host.address) &&
        Objects.equals(accessSecurity, host.accessSecurity) &&
        Objects.equals(provider, host.provider) &&
        Objects.equals(status, host.status)&&
        Objects.equals(httpHeaders, host.httpHeaders) &&
        Objects.equals(authScheme, host.authScheme) &&
        Objects.equals(authScheme, host.authScheme) &&
        Objects.equals(audit, host.audit);

  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, url, address, accessSecurity, provider, status,httpHeaders,authScheme, authScheme,audit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Host {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    accessSecurity: ").append(toIndentedString(accessSecurity)).append("\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    httpHeaders: ").append(toIndentedString(httpHeaders)).append("\n");
    sb.append("    authScheme: ").append(toIndentedString(authScheme)).append("\n");
    sb.append("    oauthInfo: ").append(toIndentedString(oauthInfo)).append("\n");
    sb.append("    audit: ").append(toIndentedString(audit)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


