classdef AffineTransformation
    %UNTITLED2 Summary of this class goes here
    %   Detailed explanation goes here
    
    properties
        tx
        ty
        rotRad
        scaleX
        scaleY
        shearX
        shearY
    end
    
    methods
        
        function obj = AffineTransformation ( tranVec)
                obj.tx  = tranVec(1);
                obj.ty  = tranVec(2);
                obj.rotRad = degtorad ( tranVec(3));
                obj.scaleX  = tranVec(4);
                obj.scaleY  = tranVec(4);
                %obj.scaleY  = tranVec(5);
                obj.shearX  = tranVec(6);
                obj.shearY  = tranVec(7);
        end
        
        % to do - unit test on that (comparing Sarits code)
        function [newX,newY] = transformPoint(self,point)
            x = point(1);
            y = point(2);
            newX = x;
            newY = y;
            % transition
            newX = newX + self.tx;
            newY = newY +self.ty;
            % rotation
            [newX,newY] = self.rotatePoint(newX,newY);
            %scale
            newX = newX*self.scaleX;
            newY = newY*self.scaleY;
            %shear
            newX = newX + self.shearX*newY;
            newY = self.shearY* newX + newY;
        end
            
        function [newX,newY] = rotatePoint(self,x,y)
            newX = x*cos(self.rotRad)+ y*sin (self.rotRad);
            newY = x*(-1)*sin(self.rotRad) + y*cos(self.rotRad);
        end              
    end
end

