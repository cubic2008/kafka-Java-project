/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.cubic.kafka.datatypes;
@org.apache.avro.specific.AvroGenerated
public enum EventStatus implements org.apache.avro.generic.GenericEnumSymbol<EventStatus> {
  Critical, Major, Minor, Warning  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"EventStatus\",\"namespace\":\"com.cubic.kafka.datatypes\",\"symbols\":[\"Critical\",\"Major\",\"Minor\",\"Warning\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}