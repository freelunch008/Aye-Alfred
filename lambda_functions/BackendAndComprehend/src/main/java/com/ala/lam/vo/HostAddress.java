package com.ala.lam.vo;

import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonProperty;


public class HostAddress 
{
	
	  private String host = null;
	  private Integer port = null;	 
	  private String path = null;
	  private ProtocolEnum protocol;
	  


		public enum ProtocolEnum {
			  
			    HTTP("HTTP"),
			    
			
			    HTTPS("HTTPS");

			    private String value;

			    ProtocolEnum(String value) {
			      this.value = value;
			    }

			    @Override
			    public String toString() {
			      return String.valueOf(value);
			    }
			  }
		  
	@JsonProperty("host")
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@JsonProperty("port")
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	@JsonProperty("path")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@JsonProperty("protocol")
	  public ProtocolEnum getProtocol() {
		return protocol;
	}
	
	  public void setProtocol(ProtocolEnum protocol) {
		this.protocol=protocol;
	}
	
	  @Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    HostAddress hostAddress = (HostAddress) o;
	    return Objects.equals(this.host, hostAddress.host) &&
	        Objects.equals(this.port, hostAddress.port) &&
	        Objects.equals(this.path, hostAddress.path) &&
	        Objects.equals(this.protocol, hostAddress.protocol);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(host,port,path,protocol);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class HostAddress {\n");
	    
	    sb.append("    host: ").append(toIndentedString(host)).append("\n");
	    sb.append("    port: ").append(toIndentedString(port)).append("\n");
	    sb.append("    port: ").append(toIndentedString(port)).append("\n");
	    sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
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
