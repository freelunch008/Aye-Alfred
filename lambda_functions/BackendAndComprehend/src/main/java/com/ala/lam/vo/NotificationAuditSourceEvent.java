package com.ala.lam.vo;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationAuditSourceEvent   {
  
  private String type = null;
  private String typeId = null;
  private String txnId=null;

  /**
   **/
  
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  
  @JsonProperty("type_id")
  public String getTypeId() {
    return typeId;
  }
  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  @JsonProperty("txn_id")
  public String getTxnId() {
	return txnId;
}
public void setTxnId(String txnId) {
	this.txnId = txnId;
}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationAuditSourceEvent notificationAuditSourceEvent = (NotificationAuditSourceEvent) o;
    return Objects.equals(type, notificationAuditSourceEvent.type) &&
        Objects.equals(typeId, notificationAuditSourceEvent.typeId)&&
        Objects.equals(txnId, notificationAuditSourceEvent.txnId) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, typeId,txnId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationAuditSourceEvent {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    txnId: ").append(toIndentedString(txnId)).append("\n");
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

