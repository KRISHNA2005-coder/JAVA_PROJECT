import { DashboardLayout } from "@/components/DashboardLayout";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Plane, Calendar, Download, MoreVertical, User } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";

const Bookings = () => {
  const [bookings, setBookings] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { toast } = useToast();

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      // Get logged-in user email from localStorage
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      const email = user.email;

      if (!email) {
        setLoading(false);
        return;
      }

      const response = await fetch(`http://localhost:8080/api/bookings?email=${email}`);
      const data = await response.json();
      setBookings(data);
    } catch (error) {
      console.error("Failed to fetch bookings:", error);
      toast({
        title: "Error",
        description: "Failed to load bookings",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDownloadTicket = async (bookingId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/bookings/${bookingId}/ticket`);
      
      if (!response.ok) {
        throw new Error('Failed to download ticket');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      
      const booking = bookings.find(b => b.id === bookingId);
      link.download = `ticket-${booking?.bookingRef || bookingId}.pdf`;
      
      document.body.appendChild(link);
      link.click();
      
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);

      toast({
        title: "Success",
        description: "Ticket downloaded successfully!",
      });
    } catch (error) {
      console.error('Download error:', error);
      toast({
        title: "Error",
        description: "Failed to download ticket. Please try again.",
        variant: "destructive",
      });
    }
  };

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <p>Loading bookings...</p>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground mb-2">My Bookings</h1>
            <p className="text-muted-foreground">
              View and manage all your flight bookings
            </p>
          </div>
          <Button onClick={() => navigate("/dashboard")}>
            <Plane className="mr-2 h-4 w-4" />
            Book New Flight
          </Button>
        </div>

        <div className="space-y-4">
          {bookings.length === 0 ? (
            <Card>
              <CardContent className="flex flex-col items-center justify-center py-12">
                <Plane className="h-12 w-12 text-muted-foreground mb-4" />
                <h3 className="text-lg font-semibold mb-2">No bookings yet</h3>
                <p className="text-sm text-muted-foreground">
                  Book your first flight to see it here
                </p>
              </CardContent>
            </Card>
          ) : (
            bookings.map((booking) => (
            <Card key={booking.id}>
              <CardHeader className="pb-3">
                <div className="flex items-start justify-between">
                  <div>
                    <CardTitle className="text-lg mb-1">
                      {booking.from} → {booking.to}
                    </CardTitle>
                    <p className="text-sm text-muted-foreground">
                      Booking ID: {booking.bookingRef}
                    </p>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant="default">
                      {booking.status}
                    </Badge>
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon">
                          <MoreVertical className="h-4 w-4" />
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem onClick={() => handleDownloadTicket(booking.id)}>
                          <Download className="mr-2 h-4 w-4" />
                          Download Ticket
                        </DropdownMenuItem>
                        <DropdownMenuItem className="text-destructive">
                          Cancel Booking
                        </DropdownMenuItem>
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                  <div>
                    <p className="text-muted-foreground mb-1">Departure</p>
                    <div className="flex items-center gap-1">
                      <Calendar className="h-4 w-4" />
                      <span className="font-medium">{booking.departure}</span>
                    </div>
                  </div>
                  <div>
                    <p className="text-muted-foreground mb-1">Arrival</p>
                    <p className="font-medium">{booking.arrival}</p>
                  </div>
                  <div>
                    <p className="text-muted-foreground mb-1">Airline</p>
                    <p className="font-medium">{booking.airline}</p>
                  </div>
                  <div>
                    <p className="text-muted-foreground mb-1">Passenger</p>
                    <div className="flex items-center gap-1">
                      <User className="h-4 w-4" />
                      <span className="font-medium">
                        {booking.passenger?.firstName} {booking.passenger?.lastName}
                      </span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
            ))
          )}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default Bookings;