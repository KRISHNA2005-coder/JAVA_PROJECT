import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { MapPin, ChevronRight } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { userAPI } from "@/services/api";

const indianStates = [
  "All India",
  "Andhra Pradesh",
  "Arunachal Pradesh",
  "Assam",
  "Bihar",
  "Chhattisgarh",
  "Goa",
  "Gujarat",
  "Haryana",
  "Himachal Pradesh",
  "Jharkhand",
  "Karnataka",
  "Kerala",
  "Madhya Pradesh",
  "Maharashtra",
  "Manipur",
  "Meghalaya",
  "Mizoram",
  "Nagaland",
  "Odisha",
  "Punjab",
  "Rajasthan",
  "Sikkim",
  "Tamil Nadu",
  "Telangana",
  "Tripura",
  "Uttar Pradesh",
  "Uttarakhand",
  "West Bengal",
];

const LocationSelection = () => {
  const [selectedLocation, setSelectedLocation] = useState("All India");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleContinue = async () => {
    setLoading(true);
    try {
      const userEmail = localStorage.getItem("userEmail");
      
      if (userEmail) {
        // Update location in backend
        await userAPI.updateLocation(userEmail, selectedLocation);
      }
      
      // Save to localStorage
      localStorage.setItem("userLocation", selectedLocation);
      
      toast({
        title: "Location set!",
        description: `You've selected ${selectedLocation}`,
      });
      
      navigate("/dashboard");
    } catch (error) {
      console.error("Failed to update location:", error);
      // Still navigate even if backend fails
      localStorage.setItem("userLocation", selectedLocation);
      navigate("/dashboard");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="w-full max-w-2xl animate-fade-in">
        <div className="bg-card rounded-xl shadow-sm border border-border p-8">
          <div className="text-center mb-8">
            <div className="mx-auto w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mb-4">
              <MapPin className="h-8 w-8 text-primary" />
            </div>
            <h1 className="text-3xl font-bold text-foreground mb-2">
              Select Your Location
            </h1>
            <p className="text-muted-foreground">
              Choose your preferred location to see relevant flight options
            </p>
          </div>

          <div className="mb-6">
            <Label className="text-base font-semibold mb-4 block">
              Select State/Region
            </Label>
            <RadioGroup
              value={selectedLocation}
              onValueChange={setSelectedLocation}
              className="grid grid-cols-2 md:grid-cols-3 gap-3 max-h-96 overflow-y-auto p-2"
            >
              {indianStates.map((state) => (
                <div key={state} className="flex items-center space-x-2">
                  <RadioGroupItem value={state} id={state} />
                  <Label
                    htmlFor={state}
                    className="cursor-pointer text-sm font-normal"
                  >
                    {state}
                  </Label>
                </div>
              ))}
            </RadioGroup>
          </div>

          <Button 
            onClick={handleContinue} 
            className="w-full" 
            size="lg"
            disabled={loading}
          >
            {loading ? "Saving..." : "Continue to Dashboard"}
            <ChevronRight className="ml-2 h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  );
};

export default LocationSelection;