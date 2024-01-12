def convertWGS84ToTM(latitude, longitude, originLongitude):
    from math import radians, sin, cos, tan, sqrt, pow
    # Constants
    a = 6378137  # Semi-major axis of the WGS84 ellipsoid
    f = 1/298.257222101  # Flattening of the WGS84 ellipsoid

    # Calculations
    e2 = (a**2 - (a * (1 - f))**2) / a**2  # Eccentricity squared
    lat_rad = latitude * pi / 180  # Convert latitude to radians
    long_rad = longitude * pi / 180  # Convert longitude to radians
    origin_long_rad = originLongitude * pi / 180  # Convert origin longitude to radians

    # Formula calculation
    y = 200000 + 1 * (a / sqrt(1 - e2 * sin(lat_rad)**2)) * \
        (((long_rad - origin_long_rad) * cos(lat_rad)) + 
         ((long_rad - origin_long_rad) * cos(lat_rad))**3 / 6 * 
         (1 - tan(lat_rad)**2 + (e2 / (1 - e2)) * cos(lat_rad)**2)) + \
        ((long_rad - origin_long_rad) * cos(lat_rad))**5 / 120 * \
        (5 - 18 * tan(lat_rad)**2 + tan(lat_rad)**4 + 
         72 * (e2 / (1 - e2)) * cos(lat_rad)**2 - 
         58 * (e2 / (1 - f)**2))

    return y