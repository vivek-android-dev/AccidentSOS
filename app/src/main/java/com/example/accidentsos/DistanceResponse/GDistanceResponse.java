package com.example.accidentsos.DistanceResponse;

import java.util.ArrayList;

public class GDistanceResponse {
    private ArrayList<String> destination_addresses;
    private ArrayList<String> origin_addresses;
    private ArrayList<Row> rows;
    private String status;
    private String error_message;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public ArrayList<String> getDestination_addresses() {
        return destination_addresses;
    }
    public void setDestination_addresses(ArrayList<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }
    public ArrayList<String> getOrigin_addresses() {
        return origin_addresses;
    }
    public void setOrigin_addresses(ArrayList<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }
    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Row{
        private ArrayList<Element> elements;

        public ArrayList<Element> getElements() {
            return elements;
        }

        public void setElements(ArrayList<Element> elements) {
            this.elements = elements;
        }

        public class Element{
            private Distance distance;
            private Duration duration;
            private DurationInTraffic duration_in_traffic;
            private String status;

            public Distance getDistance() {
                return distance;
            }

            public void setDistance(Distance distance) {
                this.distance = distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public void setDuration(Duration duration) {
                this.duration = duration;
            }

            public DurationInTraffic getDuration_in_traffic() {
                return duration_in_traffic;
            }

            public void setDuration_in_traffic(DurationInTraffic duration_in_traffic) {
                this.duration_in_traffic = duration_in_traffic;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public class Distance{
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
            public class Duration{
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
            public class DurationInTraffic{
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }

}
