classdef ImageSaver
    %UNTITLED3 Summary of this class goes here
    %   Detailed explanation goes here
    
    properties
        fullPath
        taskID
        type
    end
    
    methods
        function obj = ImageSaver ( path, taskID)
            obj.fullPath = strcat(path,'\',taskID);
            obj.taskID = taskID;
            obj.type = 'jpg';    
            mkdir(obj.fullPath );
        end
        function  do(self, image,name)
            fullName = strcat (self.taskID,'_',name,'.',self.type) ;
            imwrite(image,fullfile(self.fullPath,fullName));
      end
    end
    
end

