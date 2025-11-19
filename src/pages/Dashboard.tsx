import { DashboardLayout } from "@/components/DashboardLayout";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Plane } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { flightAPI } from "@/services/api";

const Dashboard = () => {
  const navigate = useNavigate();
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadFlights();
  }, []);

  const loadFlights = async () => {
    try {
      const data = await flightAPI.getAllFlights();
      setFlights(data);
    } catch (error) {
      console.error("Failed to load flights:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <p className="text-muted-foreground">Loading flights...</p>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">
            Available Flights
          </h1>
          <p className="text-muted-foreground">
            Browse and book from our selection of available flights
          </p>
        </div>

        <div className="space-y-4">
          {flights.map((flight) => (
            <Card key={flight.id} className="overflow-hidden">
              <CardContent className="p-0">
                <div className="flex flex-col md:flex-row">
                  <div className="md:w-64 h-48 md:h-auto overflow-hidden bg-gray-200">
                    {flight.image ? (
                      <img 
                        src={flight.image} 
                        alt={flight.airline}
                        className="w-full h-full object-cover"
                      />
                    ) : (
                      <div className="w-full h-full flex items-center justify-center text-gray-400">
                        <Plane className="h-16 w-16" />
                      </div>
                    )}
                  </div>
                  <div className="flex-1 p-6 flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
                    <div className="space-y-3 flex-1">
                      <h3 className="text-xl font-semibold text-foreground">
                        {flight.airline}
                      </h3>
                      <p className="text-sm text-muted-foreground">
                        {flight.from} → {flight.to}
                      </p>
                      <div className="flex items-center gap-8 text-sm">
                        <div>
                          <p className="text-muted-foreground mb-1">Departure</p>
                          <p className="font-semibold text-foreground">{flight.departure}</p>
                        </div>
                        <div className="flex flex-col items-center">
                          <Plane className="h-4 w-4 text-primary mb-1" />
                          <p className="text-xs text-muted-foreground">{flight.duration}</p>
                        </div>
                        <div>
                          <p className="text-muted-foreground mb-1">Arrival</p>
                          <p className="font-semibold text-foreground">{flight.arrival}</p>
                        </div>
                      </div>
                    </div>
                    <div className="flex flex-col items-end gap-3 md:ml-6">
                      <div className="text-right">
                        <p className="text-2xl font-bold text-primary">
                          {flight.price}
                        </p>
                        <p className="text-xs text-muted-foreground">per person</p>
                      </div>
                      <Button 
                        className="w-full md:w-auto"
                        onClick={() => navigate("/flight-booking", { state: { flight } })}
                      >
                        Select Flight
                      </Button>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default Dashboard;