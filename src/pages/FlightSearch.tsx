import { DashboardLayout } from "@/components/DashboardLayout";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Plane, Calendar, Users, Search } from "lucide-react";

const FlightSearch = () => {
  return (
    <DashboardLayout>
      <div className="max-w-4xl space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">Search Flights</h1>
          <p className="text-muted-foreground">
            Find the best flights for your journey
          </p>
        </div>

        <Card>
          <CardContent className="pt-6">
            <div className="space-y-6">
              <RadioGroup defaultValue="round-trip" className="flex gap-4">
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="round-trip" id="round-trip" />
                  <Label htmlFor="round-trip">Round Trip</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="one-way" id="one-way" />
                  <Label htmlFor="one-way">One Way</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="multi-city" id="multi-city" />
                  <Label htmlFor="multi-city">Multi City</Label>
                </div>
              </RadioGroup>

              <div className="grid gap-4 md:grid-cols-2">
                <div className="space-y-2">
                  <Label htmlFor="from">From</Label>
                  <div className="relative">
                    <Plane className="absolute left-3 top-3 h-4 w-4 text-muted-foreground rotate-45" />
                    <Input
                      id="from"
                      placeholder="Delhi (DEL)"
                      className="pl-10"
                    />
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="to">To</Label>
                  <div className="relative">
                    <Plane className="absolute left-3 top-3 h-4 w-4 text-muted-foreground -rotate-45" />
                    <Input
                      id="to"
                      placeholder="Mumbai (BOM)"
                      className="pl-10"
                    />
                  </div>
                </div>
              </div>

              <div className="grid gap-4 md:grid-cols-3">
                <div className="space-y-2">
                  <Label htmlFor="departure">Departure Date</Label>
                  <div className="relative">
                    <Calendar className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                    <Input id="departure" type="date" className="pl-10" />
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="return">Return Date</Label>
                  <div className="relative">
                    <Calendar className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                    <Input id="return" type="date" className="pl-10" />
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="passengers">Passengers</Label>
                  <div className="relative">
                    <Users className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                    <Input
                      id="passengers"
                      type="number"
                      defaultValue={1}
                      min={1}
                      className="pl-10"
                    />
                  </div>
                </div>
              </div>

              <Button size="lg" className="w-full">
                <Search className="mr-2 h-4 w-4" />
                Search Flights
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="text-center py-12 text-muted-foreground">
              <Plane className="h-16 w-16 mx-auto mb-4 opacity-50" />
              <p>Search results will appear here</p>
            </div>
          </CardContent>
        </Card>
      </div>
    </DashboardLayout>
  );
};

export default FlightSearch;
