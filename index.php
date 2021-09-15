<?php
if(isset($_GET["cmd"]))
{
   if($_GET["cmd"] == "create")
   {
       //Nhận Lệnh Create từ máy client
       $paths = create();
       foreach($paths as $path)
       {
           echo $path."\n";
       }
   }
}
else{
    echo "404";
}

function create($forder_parent = "resource") //return list file in folder
{
    $folder[] = "$forder_parent/*";
    while (true) {
        $DIR = [];
        foreach ($folder as $path) {
            foreach (glob($path . "/*") as $dir) {
                if (strpos($dir, ".") === false) {
                    $DIR[] = $dir;
                } else
                    $file[] = $dir;
            }
        }
        $folder = (array) $DIR;
        if ($DIR == []) break;
    }
    $result[] = $forder_parent;
    foreach($file as $f){ $result[] =  str_replace($forder_parent."/","",$f); }
    return $result;
}