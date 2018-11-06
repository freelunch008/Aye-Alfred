package com.ala.lam.vo;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationAuditSource   {
  
  private String product = null;
  private String channel = null;
  private String party = null;
  private NotificationAuditSourceEvent event = null;

  /**
   **/
  
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationAuditSource notificationAuditSource = (NotificationAuditSource) o;
    return Objects.equals(product, notificationAuditSource.product) &&
        Objects.equals(channel, notificationAuditSource.channel) &&
        Objects.equals(party, notificationAuditSource.party) &&
        Objects.equals(event, notificationAuditSource.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(product, channel, party, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationAuditSource {\n");
    
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    party: ").append(toIndentedString(party)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

