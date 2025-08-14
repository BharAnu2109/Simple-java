package com.hotel.management.common.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingEvent extends BaseEvent {
    
    public static final String BOOKING_CREATED = "BOOKING_CREATED";
    public static final String BOOKING_CONFIRMED = "BOOKING_CONFIRMED";
    public static final String BOOKING_CANCELLED = "BOOKING_CANCELLED";
    public static final String BOOKING_CHECKED_IN = "BOOKING_CHECKED_IN";
    public static final String BOOKING_CHECKED_OUT = "BOOKING_CHECKED_OUT";
    
    private Long bookingId;
    private Long guestId;
    private Long roomId;
    
    public BookingEvent() {
        super();
    }
    
    public BookingEvent(String eventType, Long bookingId, Long guestId, Long roomId, Object data) {
        super(eventType, "booking-service", data);
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.roomId = roomId;
    }
}