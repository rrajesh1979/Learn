db.neighborhoods.findOne({
    geometry: {
        $geoIntersects: {
            $geometry: {
                type: "Point",
                coordinates: [-73.93414657, 40.82302903]
            }
        }
    }
})