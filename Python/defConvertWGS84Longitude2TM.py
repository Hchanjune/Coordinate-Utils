def convertWGS84ToTMX(longitude, latitude, origin_longitude, origin_addition_x):
    from math import radians, sin, cos, tan, sqrt, pow
    
    # Constants
    a = 6378137.0  # Earth's Major Axis
    f = 1 / 298.257222101  # Flattening Factor

    # Convert degrees to radians
    lat_rad = radians(latitude)
    long_rad = radians(longitude)
    origin_long_rad = radians(origin_longitude)

    # Calculate the square of first eccentricity
    e_square = (pow(a, 2) - pow(a * (1 - f), 2)) / pow(a, 2)

    # Calculate the correction term at the origin (38 degrees)
    origin_correction_lat = radians(38.0)
    M0 = (a * ((1 - e_square / 4 - 3 * pow(e_square, 2) / 64 - 5 * pow(e_square, 3) / 256) * origin_correction_lat
                - (3 * e_square / 8 + 3 * pow(e_square, 2) / 32 + 45 * pow(e_square, 3) / 1024) * sin(2 * origin_correction_lat)
                + (15 * pow(e_square, 2) / 256 + 45 * pow(e_square, 3) / 1024) * sin(4 * origin_correction_lat)
                - (35 * pow(e_square, 3) / 3072) * sin(6 * origin_correction_lat)))

    # Calculate M (Meridian arc length)
    M = (a * ((1 - e_square / 4 - 3 * pow(e_square, 2) / 64 - 5 * pow(e_square, 3) / 256) * lat_rad
                - (3 * e_square / 8 + 3 * pow(e_square, 2) / 32 + 45 * pow(e_square, 3) / 1024) * sin(2 * lat_rad)
                + (15 * pow(e_square, 2) / 256 + 45 * pow(e_square, 3) / 1024) * sin(4 * lat_rad)
                - (35 * pow(e_square, 3) / 3072) * sin(6 * lat_rad)))

    # Calculate the correction for longitude (Nu)
    Nu = a / sqrt(1 - e_square * pow(sin(lat_rad), 2))

    # Calculate the longitude term (K)
    K = (Nu * tan(lat_rad) *
            pow((long_rad - origin_long_rad) * cos(lat_rad), 2) / 2 +
            Nu * tan(lat_rad) *
            pow((long_rad - origin_long_rad) * cos(lat_rad), 4) / 24 *
            (5 - pow(tan(lat_rad), 2) + 9 * e_square * pow(cos(lat_rad), 2) + 4 * pow(e_square * pow(cos(lat_rad), 2), 2)) +
            Nu * tan(lat_rad) *
            pow((long_rad - origin_long_rad) * cos(lat_rad), 6) / 720 *
            (61 - 58 * pow(tan(lat_rad), 2) + pow(tan(lat_rad), 4) + 270 * e_square * pow(cos(lat_rad), 2) -
            330 * e_square * pow(sin(lat_rad), 2)))

    # Calculate the final X coordinate
    X = origin_addition_x + (M - M0 + K)
    return X