import { DashboardLayout } from "@/components/DashboardLayout";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";
import { Plane, Calendar, Clock, User, Mail, Phone, CreditCard } from "lucide-react";
import { useNavigate, useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { useToast } from "@/hooks/use-toast";
import { bookingAPI } from "@/services/api";

const bookingSchema = z.object({
  firstName: z.string().trim().min(2, "First name must be at least 2 characters").max(50, "First name is too long"),
  lastName: z.string().trim().min(2, "Last name must be at least 2 characters").max(50, "Last name is too long"),
  email: z.string().trim().email("Invalid email address").max(255, "Email is too long"),
  phone: z.string().trim().min(10, "Phone number must be at least 10 digits").max(15, "Phone number is too long"),
  cardNumber: z.string().trim().min(16, "Card number must be 16 digits").max(16, "Card number must be 16 digits"),
  cardExpiry: z.string().trim().regex(/^\d{2}\/\d{2}$/, "Invalid format (MM/YY)"),
  cardCvv: z.string().trim().min(3, "CVV must be 3 digits").max(4, "CVV must be 3-4 digits"),
});

type BookingFormData = z.infer<typeof bookingSchema>;

const FlightBooking = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { toast } = useToast();
  const flight = location.state?.flight;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<BookingFormData>({
    resolver: zodResolver(bookingSchema),
  });

  if (!flight) {
    navigate("/dashboard");
    return null;
  }

  const onSubmit = async (data: BookingFormData) => {
    try {
      const bookingData = {
        flightId: flight.id,
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        phone: data.phone,
        cardNumber: data.cardNumber,
        cardExpiry: data.cardExpiry,
        cardCvv: data.cardCvv,
        paymentType: "CREDIT_CARD"
      };

      const response = await bookingAPI.createBooking(bookingData);

      if (response.success) {
        toast({
          title: "Booking Confirmed!",
          description: `Your booking reference is ${response.booking.bookingRef}`,
        });
        navigate("/bookings");
      } else {
        toast({
          title: "Booking Failed",
          description: response.message || "Unable to complete booking",
          variant: "destructive",
        });
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to connect to server. Please try again.",
        variant: "destructive",
      });
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-5xl mx-auto space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">Complete Your Booking</h1>
          <p className="text-muted-foreground">
            Review flight details and enter passenger information
          </p>
        </div>

        <div className="grid gap-6 md:grid-cols-3">
          <div className="md:col-span-2 space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>Passenger Details</CardTitle>
              </CardHeader>
              <CardContent>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                  <div className="grid gap-4 md:grid-cols-2">
                    <div className="space-y-2">
                      <Label htmlFor="firstName">First Name</Label>
                      <div className="relative">
                        <User className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                        <Input
                          id="firstName"
                          placeholder="John"
                          className="pl-10"
                          {...register("firstName")}
                        />
                      </div>
                      {errors.firstName && (
                        <p className="text-sm text-destructive">{errors.firstName.message}</p>
                      )}
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="lastName">Last Name</Label>
                      <Input
                        id="lastName"
                        placeholder="Doe"
                        {...register("lastName")}
                      />
                      {errors.lastName && (
                        <p className="text-sm text-destructive">{errors.lastName.message}</p>
                      )}
                    </div>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="email">Email Address</Label>
                    <div className="relative">
                      <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                      <Input
                        id="email"
                        type="email"
                        placeholder="john.doe@example.com"
                        className="pl-10"
                        {...register("email")}
                      />
                    </div>
                    {errors.email && (
                      <p className="text-sm text-destructive">{errors.email.message}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="phone">Phone Number</Label>
                    <div className="relative">
                      <Phone className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                      <Input
                        id="phone"
                        type="tel"
                        placeholder="9876543210"
                        className="pl-10"
                        {...register("phone")}
                      />
                    </div>
                    {errors.phone && (
                      <p className="text-sm text-destructive">{errors.phone.message}</p>
                    )}
                  </div>

                  <Separator />

                  <div>
                    <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                      <CreditCard className="h-5 w-5 text-primary" />
                      Payment Information
                    </h3>
                    <div className="space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="cardNumber">Card Number</Label>
                        <Input
                          id="cardNumber"
                          placeholder="1234567890123456"
                          maxLength={16}
                          {...register("cardNumber")}
                        />
                        {errors.cardNumber && (
                          <p className="text-sm text-destructive">{errors.cardNumber.message}</p>
                        )}
                      </div>
                      <div className="grid gap-4 md:grid-cols-2">
                        <div className="space-y-2">
                          <Label htmlFor="cardExpiry">Expiry Date</Label>
                          <Input
                            id="cardExpiry"
                            placeholder="MM/YY"
                            maxLength={5}
                            {...register("cardExpiry")}
                          />
                          {errors.cardExpiry && (
                            <p className="text-sm text-destructive">{errors.cardExpiry.message}</p>
                          )}
                        </div>
                        <div className="space-y-2">
                          <Label htmlFor="cardCvv">CVV</Label>
                          <Input
                            id="cardCvv"
                            type="password"
                            placeholder="123"
                            maxLength={4}
                            {...register("cardCvv")}
                          />
                          {errors.cardCvv && (
                            <p className="text-sm text-destructive">{errors.cardCvv.message}</p>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="flex gap-3 pt-4">
                    <Button type="submit" size="lg" className="flex-1">
                      Complete Booking
                    </Button>
                    <Button
                      type="button"
                      variant="outline"
                      size="lg"
                      onClick={() => navigate("/dashboard")}
                    >
                      Cancel
                    </Button>
                  </div>
                </form>
              </CardContent>
            </Card>
          </div>

          <div className="md:col-span-1">
            <Card className="sticky top-6">
              <CardHeader>
                <CardTitle>Flight Summary</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-3">
                  <div className="flex items-center gap-2 text-sm">
                    <Plane className="h-4 w-4 text-primary" />
                    <span className="font-semibold">{flight.airline}</span>
                  </div>
                  <p className="text-sm text-muted-foreground">
                    {flight.from} → {flight.to}
                  </p>
                </div>

                <Separator />

                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground flex items-center gap-2">
                      <Calendar className="h-3 w-3" />
                      Departure
                    </span>
                    <span className="font-medium">{flight.departure}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground flex items-center gap-2">
                      <Calendar className="h-3 w-3" />
                      Arrival
                    </span>
                    <span className="font-medium">{flight.arrival}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground flex items-center gap-2">
                      <Clock className="h-3 w-3" />
                      Duration
                    </span>
                    <span className="font-medium">{flight.duration}</span>
                  </div>
                </div>

                <Separator />

                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">Base Fare</span>
                    <span>{flight.price}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">Taxes & Fees</span>
                    <span>₹500</span>
                  </div>
                  <Separator />
                  <div className="flex justify-between text-lg font-bold">
                    <span>Total</span>
                    <span className="text-primary">
                      ₹{parseInt(flight.price.replace(/[₹,]/g, "")) + 500}
                    </span>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default FlightBooking;