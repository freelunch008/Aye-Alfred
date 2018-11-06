package com.ala.lam.vo;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


public class NotificationAudit   
{
  

  private String creator = null;
  private Long timestamp = null;
  private String product = null;
  private String channel = null;
  private String party = null;
  private NotificationAuditSourceEvent event = null;
  private String partnerKey = null;
  


/**
   **/

  
  @JsonProperty("creator")
  public String getCreator() {
    return creator;
  }
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   **/
  
  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  @JsonProperty("product")
  public String getProduct() {
    return product;
  }
  public void setProduct(String product) {
    this.product = product;
  }

  /**
   **/
  
  @JsonProperty("channel")
  public String getChannel() {
    return channel;
  }
  public void setChannel(String channel) {
    this.channel = channel;
  }

  /**
   **/
  
  @JsonProperty("party")
  public String getParty() {
    return party;
  }
  public void setParty(String party) {
    this.party = party;
  }

  /**
   **/
  
  @JsonProperty("event")
  public NotificationAuditSourceEvent getEvent() {
    return event;
  }
  public void setEvent(NotificationAuditSourceEvent event) {
    this.event = event;
  }
  
  @JsonProperty("client_key")  
  public String getPartnerKey() {
	return partnerKey;
}
public void setPartnerKey(String partnerKey) {
	this.partnerKey = partnerKey;
}


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationAudit notificationAudit = (NotificationAudit) o;
    return Objects.equals(product, notificationAudit.product) &&
            Objects.equals(channel, notificationAudit.channel) &&
            Objects.equals(party, notificationAudit.party) &&
            Objects.equals(event, notificationAudit.event)&&
        Objects.equals(creator, notificationAudit.creator) &&
        Objects.equals(partnerKey, notificationAudit.partnerKey) &&
        Objects.equals(timestamp, notificationAudit.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash( product, channel, party, event,creator,partnerKey, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationAudit {\n");
    
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    party: ").append(toIndentedString(party)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    partnerKey: ").append(toIndentedString(partnerKey)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

