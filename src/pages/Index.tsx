import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Plane } from "lucide-react";

const Index = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800 flex items-center justify-center p-4">
      <div className="text-center animate-fade-in max-w-2xl">
        <div className="flex items-center justify-center mb-6">
          <Plane className="h-16 w-16 text-primary animate-pulse" />
        </div>
        <h1 className="mb-4 text-5xl md:text-6xl font-bold text-foreground">
          SkyBook Airlines
        </h1>
        <p className="text-xl text-muted-foreground mb-8">
          Airline Reservation System - Book your flights with ease
        </p>
        <div className="flex gap-4 justify-center">
          <Link to="/login">
            <Button size="lg" className="group">
              Sign In
              <Plane className="ml-2 h-4 w-4 group-hover:translate-x-1 transition-transform" />
            </Button>
          </Link>
          <Link to="/register">
            <Button size="lg" variant="outline">
              Create Account
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Index;